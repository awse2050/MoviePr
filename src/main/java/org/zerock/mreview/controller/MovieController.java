package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.service.MovieService;

@Controller
@Log4j2
@RequestMapping("/movie/*")
public class MovieController {

    @Autowired
    private MovieService service;

    @GetMapping("/register")
    public void getRegister() {
        log.info("get register....");
    }

    @PostMapping("/register")
    public String register(MovieDTO movieDTO, RedirectAttributes rttr) {
        log.info("regist movieDTO : " + movieDTO);

        Long mno = service.register(movieDTO);

        rttr.addFlashAttribute("msg", mno);

        return "redirect:/movie/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("pageRequestDTO : " + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));

    }

    @GetMapping({"/read","/modify"})
    public void get(Long mno, @ModelAttribute("requestDTO")PageRequestDTO requestDTO, Model model) {
        log.info("get mno ... : " + mno);

        MovieDTO movieDTO = service.getMoive(mno);

        model.addAttribute("dto", movieDTO);
    }
}
