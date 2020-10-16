package com.yipin.basic.controller.admin;

import com.yipin.basic.dao.othersDao.AdminRepository;
import com.yipin.basic.dao.othersDao.ArtActivityRepository;
import com.yipin.basic.dao.othersDao.CommentRepository;
import com.yipin.basic.dao.othersDao.SlideRepository;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.productionDao.ProductionTagRepository;
import com.yipin.basic.dao.specialistDao.PaintTypeRepository;
import com.yipin.basic.dao.specialistDao.SpecialistRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.others.Admin;
import com.yipin.basic.entity.others.ArtActivity;
import com.yipin.basic.entity.others.Comment;
import com.yipin.basic.entity.others.Slide;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.production.ProductionTag;
import com.yipin.basic.entity.specialist.PaintType;
import com.yipin.basic.entity.specialist.Specialist;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.form.AdminRegisterForm;
import com.yipin.basic.form.ArtActivityForm;
import com.yipin.basic.service.ArtActivityService;
import com.yipin.basic.service.UserService;
import com.yipin.basic.utils.MarkdownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductionRepository productionRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpecialistRepository specialistRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SlideRepository slideRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PaintTypeRepository paintTypeRepository;
    @Autowired
    private ProductionTagRepository productionTagRepository;
    @Autowired
    private ArtActivityService artActivityService;
    @Autowired
    private ArtActivityRepository artActivityRepository;

    @PostMapping("/addAdminUser")
    public String addAdminUser(AdminRegisterForm adminRegisterForm, RedirectAttributes attributes) {
        Admin a = adminRepository.findAdminByUsername(adminRegisterForm.getUsername());
        if (a != null) {
            attributes.addFlashAttribute("msg", "该用户名已存在");
            return "redirect:/admin/admin-register";
        }
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminRegisterForm, admin);
        admin.setCreateTime(new Date());
        admin.setUpdateTime(new Date());
        admin.setIsSuper(0);
        admin.setPassword(encoder.encode(adminRegisterForm.getPassword()));
        adminRepository.save(admin);
        attributes.addFlashAttribute("msg", "添加成功！");
        return "redirect:/admin/admin-list";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam Integer id, @RequestParam String password, HttpServletRequest request, RedirectAttributes attributes) {
        Admin admin = adminRepository.findAdminById(id);
        admin.setPassword(encoder.encode(password));
        admin.setUpdateTime(new Date());
        adminRepository.save(admin);
        Admin a = (Admin) request.getSession().getAttribute("user");
        if (a.getId() == id) {
            request.getSession().removeAttribute("user");
            return "redirect:/admin";
        }
        attributes.addFlashAttribute("msg", "修改成功！");
        return "redirect:/admin/admin-list";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request,
                        RedirectAttributes attributes) {
        Admin admin = adminRepository.findAdminByUsername(username);
        if (admin == null) {
            attributes.addFlashAttribute("msg", "用户名不存在");
            return "redirect:/admin";  //重新定向到/admin
        }
        if (encoder.matches(password, admin.getPassword())) {
            admin.setUpdateTime(new Date());
            adminRepository.save(admin);
            request.getSession().setAttribute("user", admin);
            return "redirect:/admin/index";
        } else {
            attributes.addFlashAttribute("msg", "密码错误");
            return "redirect:/admin";  //重新定向到/admin
        }
    }

    /**
     * 删除作品
     **/
    @PostMapping("/production-list/delete")
    public String deleteProduction(@RequestParam Integer id,
                                   RedirectAttributes attributes) {
        Production production = productionRepository.findProductionById(id);
        if (production.getPublishStatus() == 1) {
            User user = userRepository.findUserById(production.getUserId());
            user.setProductionNum(user.getProductionNum() - 1);
            //如果为代表作，则取消代表作状态
            if (production.getIsMainProduction() == 1){
                production.setIsMainProduction(0);
                List<Integer> ids = user.getMainProductionId();
                ids.remove(production.getId());
                user.setMainProductionId(ids);
            }
            userRepository.save(user);
        }
        production.setDeleteStatus(1);
        productionRepository.save(production);
        attributes.addFlashAttribute("msg", "成功删除作品！");
        return "redirect:/admin/production-list";
    }

    /**
     * 删除评论
     **/
    @PostMapping("/comment-list/delete")
    public String deleteComment(@RequestParam Integer id,
                                RedirectAttributes attributes) {
        Comment comment = commentRepository.findCommentById(id);
        Production production = productionRepository.findProductionById(comment.getProductionId());
        if (comment == null || comment.getDeleteStatus()) {
            attributes.addFlashAttribute("msg", "评论不存在或已被删除！");
            return "redirect:/admin/comment-list?id=" + comment.getProductionId();
        }
        comment.setDeleteStatus(true);
        commentRepository.save(comment);
        production.setComments(production.getComments() - 1);
        productionRepository.save(production);
        attributes.addFlashAttribute("msg", "成功删除评论！");
        return "redirect:/admin/comment-list?id=" + comment.getProductionId();
    }

    @RequestMapping("/searchByUserId")
    public String searchComment(Integer userId, @PageableDefault(size = 10) Pageable pageable, Model model) {
        model.addAttribute("production", null);
        model.addAttribute("page", commentRepository.findCommentByUserIdOrderByLikesDesc(userId, pageable));
        return "comment-list";
    }

    @RequestMapping("/searchByKey")
    public String searchProduction(String key, @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<Production> production = productionRepository.findProductionByTitleLikes("%" + key + "%", pageable);
        model.addAttribute("page", production);
        return "production-list";
    }

    @RequestMapping("/searchById")
    public String searchUser(Integer userId, @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<User> userPage = userRepository.findUserById(userId, pageable);
        model.addAttribute("page", userPage);
        return "user-list";
    }


    @PostMapping("/specialist/{id}/agree")
    public String agreeSpecialist(@PathVariable Integer id, RedirectAttributes attributes) {
        Specialist specialist = specialistRepository.findSpecialistById(id);
        specialist.setCheckStatus(1);
        specialistRepository.save(specialist);
        User user = userRepository.findUserById(specialist.getUserId());
        user.setIsSpecialist(1);
        userRepository.save(user);
        attributes.addFlashAttribute("msg", "已同意！");
        return "redirect:/admin/specialist";
    }

    @PostMapping("/specialist/{id}/disagree")
    public String disagreeSpecialist(@PathVariable Integer id, RedirectAttributes attributes) {
        Specialist specialist = specialistRepository.findSpecialistById(id);
        specialist.setCheckStatus(2);
        specialistRepository.save(specialist);
        attributes.addFlashAttribute("msg", "已拒绝！");
        return "redirect:/admin/specialist";
    }

    /**
     * 删除轮播图
     **/
    @PostMapping("/slide/delete")
    public String deleteSlide(@RequestParam Integer id,
                              RedirectAttributes attributes) {
        Slide slide = slideRepository.findSlideById(id);
        if (slide == null) {
            attributes.addFlashAttribute("msg", "轮播图信息不存在！");
            return "redirect:/admin/slide";
        }
        slideRepository.delete(slide);
        attributes.addFlashAttribute("msg", "成功删除轮播图！");
        return "redirect:/admin/slide";
    }

    /**
     * 上传轮播图
     **/
    @PostMapping("/slide/add")
    public String addSlide(@RequestParam("file") MultipartFile file, @RequestParam("orderNum") Integer orderNum, @RequestParam("remark") String remark,
                           RedirectAttributes attributes) {
        Slide slide = new Slide();
        slide.setOrderNum(orderNum);
        slide.setRemark(remark);
        String url = userService.uploadImage(file).getData().get("imageUrl");
        slide.setUrl(url);
        slideRepository.save(slide);
        attributes.addFlashAttribute("msg", "成功添加轮播图！");
        return "redirect:/admin/slide";
    }

    /**
     * 删除画种
     **/
    @PostMapping("/paintType/delete")
    public String deletePaintType(@RequestParam Integer id,
                                  RedirectAttributes attributes) {
        PaintType paintType = paintTypeRepository.findPaintTypeById(id);
        if (paintType == null) {
            attributes.addFlashAttribute("msg", "画种信息不存在！");
            return "redirect:/admin/paint-type";
        }
        paintTypeRepository.delete(paintType);
        attributes.addFlashAttribute("msg", "成功删除！");
        return "redirect:/admin/paint-type";
    }

    /**
     * 上传画种信息
     **/
    @PostMapping("/paintType/add")
    public String addPaintType(@RequestParam("typeName") String typeName,
                               RedirectAttributes attributes) {
        PaintType paintType = new PaintType();
        paintType.setTypeName(typeName);
        paintTypeRepository.save(paintType);
        attributes.addFlashAttribute("msg", "成功上传！");
        return "redirect:/admin/paint-type";
    }

    /**
     * 删除分类标签
     **/
    @PostMapping("/productionTag/delete")
    public String deleteProductionTag(@RequestParam Integer id,
                                  RedirectAttributes attributes) {
        ProductionTag productionTag = productionTagRepository.findProductionTagById(id);
        if (productionTag == null) {
            attributes.addFlashAttribute("msg", "分类标签信息不存在！");
            return "redirect:/admin/production-tag";
        }
        productionTagRepository.delete(productionTag);
        attributes.addFlashAttribute("msg", "成功删除！");
        return "redirect:/admin/production-tag";
    }

    /**
     * 上传画种信息
     **/
    @PostMapping("/productionTag/add")
    public String addProductionTag(@RequestParam("tagName") String tagName,@RequestParam("orderNum") Integer orderNum,
                               RedirectAttributes attributes) {
        ProductionTag productionTag = new ProductionTag();
        productionTag.setTagName(tagName);
        productionTag.setOrderNum(orderNum);
        productionTagRepository.save(productionTag);
        attributes.addFlashAttribute("msg", "成功上传！");
        return "redirect:/admin/production-tag";
    }

    @PostMapping("/topicArticle/add")
    public String addTopicArticle(@RequestParam("title") String title,
                                  @RequestParam("content") String content,
                                  @DateTimeFormat(pattern = "yy-mm-dd") @RequestParam("startTime") Date startTime,
                                  @DateTimeFormat(pattern = "yy-mm-dd") @RequestParam("endTime") Date endTime,
                                  @RequestParam("cost") BigDecimal cost,
                                  @RequestParam("file") MultipartFile file,
                                  RedirectAttributes attributes) {
        String url = userService.uploadImage(file).getData().get("imageUrl");
        ArtActivityForm topicArticleForm = new ArtActivityForm();
        topicArticleForm.setTitle(title);
        topicArticleForm.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        topicArticleForm.setTopicImage(url);
        topicArticleForm.setEndTime(endTime);
        topicArticleForm.setStartTime(startTime);
        topicArticleForm.setCost(cost);
        artActivityService.addTopicArticle(topicArticleForm);
        attributes.addFlashAttribute("msg", "成功添加！");
        return "redirect:/admin/topicArticle";
    }

    /**
     * 删除话题
     **/
    @PostMapping("/topicArticle/delete")
    public String deleteTopicArticle(@RequestParam Integer id,
                                      RedirectAttributes attributes) {
        ArtActivity topicArticle = artActivityRepository.findArtActivityById(id);
        if (topicArticle == null) {
            attributes.addFlashAttribute("msg", "话题信息不存在！");
            return "redirect:/admin/topicArticle";
        }
        artActivityRepository.delete(topicArticle);
        attributes.addFlashAttribute("msg", "成功删除！");
        return "redirect:/admin/topicArticle";
    }
}
