package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.TagServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/tags")
public class TagController {
    private final TagServiceI tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("list_of_tags", tagService.read());
        return "/tag/show_all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model) {
        model.addAttribute("tag", tagService.read(id));
        return "/tag/show";
    }

    @PostMapping("/{id}")
    public String doNothing(@PathVariable("id") int id,
                       Model model) {
        return "/tag/show";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id){
        Tag tag=new Tag();
        tag.setId(id);
        tagService.delete(tag);
        return "redirect:/tags";
    }

    @GetMapping("/delete/{id}")
    public String doN(@PathVariable("id") long id){
        Tag tag=new Tag();
        tag.setId(id);
        tagService.delete(tag);
        return "redirect:/tags";
    }

    @PostMapping("")
    public String create(@ModelAttribute("tag") Tag tag,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "tags/new";

        tagService.create(tag);
        return "redirect:/tags";
    }

    @GetMapping("/new")
    public String showEdit(@ModelAttribute("tag") Tag tag) {
        return "/tag/new";
    }
}
