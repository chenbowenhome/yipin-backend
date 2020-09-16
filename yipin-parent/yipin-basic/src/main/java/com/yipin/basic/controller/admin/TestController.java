package com.yipin.basic.controller.admin;

import com.yipin.basic.dao.othersDao.ImgUrlRepository;
import com.yipin.basic.entity.others.ImgUrl;
import com.yipin.basic.service.UserService;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.yipin.basic.utils.others.CompareHist;

import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private UserService userService;
    @Autowired
    private ImgUrlRepository imgUrlRepository;

    @RequestMapping("/uploadImg")
    public String uploadImgPage(Model model) {
        double[] a = {0,0,0,0,0,0,0,0};
        model.addAttribute("img",a);
        model.addAttribute("imgs",imgUrlRepository.findAll());
        return "uploadImg";
    }
    @PostMapping("/test")
    public String test(@RequestParam("file") MultipartFile file,Model model) {
        String path = userService.uploadImageTest(file).getData();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat colors = new Mat(1, 8, CvType.CV_8UC3);
        byte[] colorsData = {0, 0, -1, 0, -1, 0, -1, 0, 0, -1, -1, 0, -1, 0, -1, 0, -1, -1, 0, 0, 0, -1, -1, -1};
        colors.put(0, 0, colorsData);
        int[] arr = CompareHist.colorStat(path,colors);
        double[] colorStatsNormal = new double[arr.length];
        double total = 0;
        for(int i = 0;i < arr.length;i++)
        {
            total += arr[i];
        }
        for(int i = 0;i < colorStatsNormal.length;i++)
        {
            colorStatsNormal[i] = (int)(arr[i] * 10000.0 / total) / 100.0;
        }
        ImgUrl imgUrl = new ImgUrl();
        imgUrl.setUrl(path);
        imgUrl.setImgSize(file.getSize());
        imgUrl.setRelativeUrl("/images/" + file.getOriginalFilename());
        imgUrlRepository.save(imgUrl);
        model.addAttribute("imgs",imgUrlRepository.findAll());
        model.addAttribute("imgUrl","/images/" + file.getOriginalFilename());
        model.addAttribute("img",colorStatsNormal);
        model.addAttribute("id",imgUrl.getId());
        return "uploadImg";
    }
    @PostMapping("/test2")
    public String test2(@RequestParam("path") String path,@RequestParam("url") String url,Model model) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat colors = new Mat(1, 8, CvType.CV_8UC3);
        byte[] colorsData = {0, 0, -1, 0, -1, 0, -1, 0, 0, -1, -1, 0, -1, 0, -1, 0, -1, -1, 0, 0, 0, -1, -1, -1};
        colors.put(0, 0, colorsData);
        int[] arr = CompareHist.colorStat(path,colors);
        double[] colorStatsNormal = new double[arr.length];
        double total = 0;
        for(int i = 0;i < arr.length;i++)
        {
            total += arr[i];
        }
        for(int i = 0;i < colorStatsNormal.length;i++)
        {
            colorStatsNormal[i] = (int)(arr[i] * 10000.0 / total) / 100.0;
        }
        List<ImgUrl> imgUrl = imgUrlRepository.findAllByUrl(path);
        model.addAttribute("imgs",imgUrlRepository.findAll());
        model.addAttribute("imgUrl",url);
        model.addAttribute("img",colorStatsNormal);
        model.addAttribute("id",imgUrl.get(0).getId());
        return "uploadImg";
    }
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id){
        System.out.println(id);
        imgUrlRepository.deleteById(id);
        return "redirect:/test/uploadImg";
    }
}
