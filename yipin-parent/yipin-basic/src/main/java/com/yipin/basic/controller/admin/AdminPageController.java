package com.yipin.basic.controller.admin;

import com.yipin.basic.VO.UserVO;
import com.yipin.basic.dao.othersDao.AdminRepository;
import com.yipin.basic.dao.othersDao.CommentRepository;
import com.yipin.basic.dao.othersDao.DictionariesRepository;
import com.yipin.basic.dao.othersDao.SlideRepository;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.specialistDao.SpecialistRepository;
import com.yipin.basic.dao.userDao.UserArtRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.others.Admin;
import com.yipin.basic.entity.others.Slide;
import com.yipin.basic.entity.production.Production;
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
        Production production = productionRepository.findProductionById(user.getMainProductionId());
        model.addAttribute("production",production);
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
        model.addAttribute("production",productionRepository.findProductionById(id));
        model.addAttribute("page",commentRepository.findCommentByProductionIdOrderByCreateTimeDesc(id,pageable));
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
        model.addAttribute("page",specialistRepository.findSpecialistByCheckStatusOrderByCreateTimeDesc(1,pageable));
        return "specialist-list";
    }

    @RequestMapping("/specialist-detail")
    public String specialistDetailPage(HttpServletRequest request,@RequestParam(value = "id") Integer id, Model model){
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        model.addAttribute("photo",specialistRepository.findSpecialistById(id));
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
        List<Slide> slideList = slideRepository.findSlideByOrderByOrderNumDesc();
        model.addAttribute("slideList",slideList);
        return "slide";
    }

    @GetMapping("/add-slide")
    public String addSlidePage(HttpServletRequest request,Model model) {
        Admin a = (Admin) request.getSession().getAttribute("user");
        model.addAttribute("admin",a);
        return "add-slide";
    }
}
