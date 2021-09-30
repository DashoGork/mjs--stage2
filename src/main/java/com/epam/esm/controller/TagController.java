package com.epam.esm.controller;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/tags")
public class TagController {
    private final TagDao tagDao;

    @Autowired
    public TagController(TagDaoImplementation tagDao) {
        this.tagDao = tagDao;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("list_of_tags", tagDao.read());
        return "/tag/show_all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model) {
        model.addAttribute("tag", tagDao.read(id));
        return "/tag/show";
    }

    @PostMapping("/{id}")
    public String doNothing(@PathVariable("id") int id,
                       Model model) {
        return "/tag/show";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        tagDao.delete(id);
        return "redirect:/tags";
    }

    @GetMapping("/delete/{id}")
    public String doN(@PathVariable("id") int id){
        tagDao.delete(id);
        return "redirect:/tags";
    }

    @PostMapping("")
    public String create(@ModelAttribute("tag") @Valid Tag tag,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "tags/new";

        tagDao.create(tag.getName());
        return "redirect:/tags";
    }

    @GetMapping("/new")
    public String showEdit(@ModelAttribute("tag") Tag tag) {
        return "/tag/new";
    }
}
