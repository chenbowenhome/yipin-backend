package com.yipin.basic.controller.admin;

import com.yipin.basic.dao.CommentRepository;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.specialistDao.SpecialistRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.Comment;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.specialist.Specialist;
import com.yipin.basic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    /**删除作品**/
    @GetMapping("/production-list/{id}/delete")
    public String deleteProduction(@PathVariable Integer id,
                              RedirectAttributes attributes) {
        Production production = productionRepository.findProductionById(id);
        if (production.getPublishStatus() == 1) {
            User user = userRepository.findUserById(production.getUserId());
            if (user.getMainProductionId() == production.getId()) {
                List<Production> productionList = productionRepository.findProductionByPublishStatusAndUserIdAndEvaluateStatusOrderByCreateTimeDesc(1, user.getId(), 1);
                if (productionList.isEmpty()) {
                    user.setMainProductionId(null);
                } else {
                    user.setMainProductionId(productionList.get(0).getId());
                    Production p = productionRepository.findProductionById(productionList.get(0).getId());
                    p.setIsMainProduction(1);
                    productionRepository.save(p);
                }
            }
            user.setProductionNum(user.getProductionNum() - 1);
            userRepository.save(user);
        }
        productionRepository.deleteById(id);
        attributes.addFlashAttribute("msg", "成功删除作品！");
        return "redirect:/admin/production-list";
    }

    @GetMapping("/comment-list/{id}/delete")
    public String deleteComment(@PathVariable Integer id,
                              RedirectAttributes attributes) {
        Comment comment = commentRepository.findCommentById(id);
        Production production = productionRepository.findProductionById(comment.getProductionId());
        commentRepository.deleteById(id);
        production.setComments(production.getComments() - 1);
        productionRepository.save(production);
        attributes.addFlashAttribute("msg", "成功删除评论！");
        return "redirect:/admin/comment-list?id=" + comment.getProductionId();
    }

    @RequestMapping("/searchByUserId")
    public String searchComment(Integer userId, @PageableDefault(size = 10) Pageable pageable, Model model) {
        model.addAttribute("production",null);
        model.addAttribute("page",commentRepository.findCommentByUserIdOrderByCreateTimeDesc(userId,pageable));
        return "comment-list";
    }
    @RequestMapping("/searchByKey")
    public String searchProduction(String key, @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<Production> production = productionRepository.findProductionByTitleLike("%" + key + "%",pageable);
        model.addAttribute("page",production);
        return "production-list";
    }
    @RequestMapping("/searchById")
    public String searchUser(Integer userId, @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<User> userPage = userRepository.findUserById(userId,pageable);
        model.addAttribute("page",userPage);
        return "user-list";
    }

    @GetMapping("/user-list/{id}/setRole")
    public String setRole(@PathVariable Integer id) {
        User user = userRepository.findUserById(id);
        if (user.getRole() == 1){
            user.setRole(0);
        }else {
            user.setRole(1);
        }
        userRepository.save(user);
        return "redirect:/admin/user-list";
    }

    @GetMapping("/specialist/{id}/agree")
    public String agreeSpecialist(@PathVariable Integer id) {
        Specialist specialist = specialistRepository.findSpecialistById(id);
        specialist.setCheckStatus(1);
        specialistRepository.save(specialist);
        User user = userRepository.findUserById(specialist.getUserId());
        user.setIsSpecialist(1);
        userRepository.save(user);
        return "redirect:/admin/specialist";
    }

    @GetMapping("/specialist/{id}/disagree")
    public String disagreeSpecialist(@PathVariable Integer id) {
        Specialist specialist = specialistRepository.findSpecialistById(id);
        specialist.setCheckStatus(2);
        specialistRepository.save(specialist);
        return "redirect:/admin/specialist";
    }
}
