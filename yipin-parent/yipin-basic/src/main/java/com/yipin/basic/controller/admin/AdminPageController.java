package com.yipin.basic.controller.admin;

import com.yipin.basic.VO.UserVO;
import com.yipin.basic.dao.othersDao.*;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.productionDao.ProductionTagRepository;
import com.yipin.basic.dao.specialistDao.PaintTypeRepository;
import com.yipin.basic.dao.specialistDao.SpecialistRepository;
import com.yipin.basic.dao.userDao.UserArtRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.others.Admin;
import com.yipin.basic.entity.others.ArtActivity;
import com.yipin.basic.entity.others.Slide;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.production.ProductionTag;
import com.yipin.basic.entity.specialist.PaintType;
import com.yipin.basic.entity.specialist.Specialist;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.entity.user.UserArt;
import com.yipin.basic.entity.user.UserPerformance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserArtRepository userArtRepository;
    @Autowired
    private UserPerformanceRepository userPerformanceRepository;
    @Autowired
    private ProductionRepository productionRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SpecialistRepository specialistRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private DictionariesRepository dictionariesRepository;
    @Autowired
    private SlideRepository slideRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private PaintTypeRepository paintTypeRepository;
    @Autowired
    private ProductionTagRepository productionTagRepository;
    @Autowired
    private ArtActivityRepository artActivityRepository;

    @GetMapping("/index")
    public String indexPage(HttpServletRequest request,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        return "index";
    }

    @GetMapping("/user-list")
    public String userListPage(HttpServletRequest request,@PageableDefault(size = 10) Pageable pageable,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        model.addAttribute("page",userRepository.findUserByOrderByRegdateDesc(pageable));
        return "user-list";
    }

    /**用户详情**/
    @GetMapping("/user-detail")
    public String userDetailPage(@RequestParam(value = "id") Integer id,HttpServletRequest request,Model model,RedirectAttributes attributes) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        User user = userRepository.findUserById(id);
        if (user == null){
            attributes.addFlashAttribute("msg", "用户不存在，可能已被删除");
            return "user-list";
        }
        UserArt userArt = userArtRepository.findLastUserArt(user.getId());
        UserPerformance userPerformance = userPerformanceRepository.findSecondUserPerformance(user.getId());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        userVO.setUserArt(userArt);
        userVO.setUserPerformance(userPerformance);
        model.addAttribute("user",userVO);
        //查询代表作信息
        List<Production> productions = productionRepository.findProductionByIsMainProductionAndUserIdOrderByCreateTimeDesc(1,user.getId());
        model.addAttribute("production",productions);
        //查询用户公开作品信息
        List<Production> publishedProductionList = productionRepository.findProductionByPublishStatusAndUserIdOrderByCreateTimeDesc(1,id);
        //查询用户未公开作品信息
        List<Production> unPublishedProductionList = productionRepository.findProductionByPublishStatusAndUserIdOrderByCreateTimeDesc(0,id);
        model.addAttribute("publishedList",publishedProductionList);
        model.addAttribute("unPublishedList",unPublishedProductionList);
        return "user-detail";
    }

    @GetMapping("/production-list")
    public String productionListPage(HttpServletRequest request, @PageableDefault(size = 10) Pageable pageable,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        Page<Production> production = productionRepository.findProductionByOrderByCreateTimeDesc(pageable);
        model.addAttribute("page",production);
        return "production-list";
    }

    @RequestMapping("/comment-list")
    public String commentListPage(HttpServletRequest request,@RequestParam(value = "id") Integer id,@PageableDefault(size = 10) Pageable pageable, Model model){
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        Production production = productionRepository.findProductionById(id);
        User user = userRepository.findUserById(production.getUserId());
        model.addAttribute("user",user);
        model.addAttribute("production",production);
        model.addAttribute("page",commentRepository.findCommentByProductionIdOrderByLikesDesc(id,pageable));
        System.out.println(userRepository.findUserListByProductionId(id,pageable));
        return "comment-list";
    }

    @RequestMapping("/specialist")
    public String specialistPage(HttpServletRequest request,@PageableDefault(size = 10) Pageable pageable, Model model){
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        model.addAttribute("page",specialistRepository.findSpecialistByCheckStatusOrderByCreateTimeDesc(0,pageable));
        return "specialist";
    }

    @RequestMapping("/specialist-list")
    public String specialistListPage(HttpServletRequest request,@PageableDefault(size = 10) Pageable pageable, Model model){
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        Page<Specialist> specialists = specialistRepository.findSpecialistByCheckStatusOrderByCreateTimeDesc(1,pageable);
        model.addAttribute("page",specialists);
        return "specialist-list";
    }

    @RequestMapping("/specialist-detail")
    public String specialistDetailPage(HttpServletRequest request,@RequestParam(value = "id") Integer id, Model model){
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        Specialist specialist = specialistRepository.findSpecialistById(id);
        List<Integer> typeNums = specialist.getAdeptPaint();
        List<String> types = new ArrayList<>();
        for (Integer typeNum : typeNums) {
            PaintType paintType = paintTypeRepository.findPaintTypeById(typeNum);
            if (paintType != null) {
                String type = paintType.getTypeName();
                types.add(type);
            }
        }
        model.addAttribute("types",types);
        model.addAttribute("photo",specialist);
        return "specialist-detail";
    }

    @GetMapping
    public String loginPage(Model model){
        return "login";
    }

    @GetMapping("/admin-list")
    public String adminListPage(HttpServletRequest request, Model model){
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        List<Admin> adminPage = adminRepository.findAdminByOrderByCreateTime();
        model.addAttribute("page",adminPage);
        return "admin-list";
    }

    @GetMapping("/admin-register")
    public String adminRegisterPage(Model model){
        return "admin-register";
    }

    @GetMapping("/dictionaries-list")
    public String dictionariesListPage(HttpServletRequest request,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        model.addAttribute("page",dictionariesRepository.findAll());
        return "dictionaries-list";
    }

    @GetMapping("/slide")
    public String slidePage(HttpServletRequest request,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        List<Slide> slideList = slideRepository.findSlideByOrderByOrderNumAsc();
        model.addAttribute("slideList",slideList);
        return "slide";
    }

    @GetMapping("/add-slide")
    public String addSlidePage(HttpServletRequest request,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        return "add-slide";
    }

    @RequestMapping("/likes")
    public String likesPage(HttpServletRequest request,@RequestParam(value = "id") Integer id,@PageableDefault(size = 10) Pageable pageable, Model model){
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        Page<User> page = userRepository.findUserListByProductionId(id,pageable);
        model.addAttribute("page",page);
        model.addAttribute("productionId",id);
        return "likes";
    }

    @GetMapping("/paint-type")
    public String paintTypePage(HttpServletRequest request,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        model.addAttribute("paintType",paintTypeRepository.findAll());
        return "paint-type";
    }

    @GetMapping("/add-paint-type")
    public String addPaintTypePage(HttpServletRequest request,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        return "add-paint-type";
    }

    @GetMapping("/production-tag")
    public String productionTagPage(HttpServletRequest request,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        List<ProductionTag> productionTagList = productionTagRepository.findProductionTagByOrderByOrderNumAsc();
        model.addAttribute("tags",productionTagList);
        return "production-tag";
    }

    @GetMapping("/add-production-tag")
    public String addProductionTagPage(HttpServletRequest request,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        return "add-production-tag";
    }

    @GetMapping("/add-topicArticle")
    public String addTopicArticlePage(HttpServletRequest request,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        return "add-topicArticle";
    }

    @GetMapping("/topicArticle")
    public String topicArticlePage(HttpServletRequest request,@PageableDefault(size = 10) Pageable pageable,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        Page<ArtActivity> topicArticlePage = artActivityRepository.findArtActivityByOrderByCreateTimeDesc(pageable);
        model.addAttribute("topicArticlePage",topicArticlePage);
        return "topicArticle";
    }

    @RequestMapping("/topicArticle-detail")
    public String topicArticleDetailPage(HttpServletRequest request,@RequestParam(value = "id") Integer id,@PageableDefault(size = 10) Pageable pageable, Model model,
                                         RedirectAttributes attributes){
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        ArtActivity topicArticle = artActivityRepository.findArtActivityById(id);
        if (topicArticle == null){
            attributes.addFlashAttribute("msg", "文章不存在！");
            return "redirect:/admin/topicArticle";
        }
        model.addAttribute("topicArticle",topicArticle);
        return "topicArticle-detail";
    }
}
