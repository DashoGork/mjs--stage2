package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.service.tag.TagServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagServiceI tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("")
    public List<Tag> showAll() {
        return tagService.read();
    }

    @GetMapping("/{id}")
    public Tag show(@PathVariable("id") int id) {
        return tagService.read(id);
    }

    @DeleteMapping("/delete/{id}")
    public List<Tag> delete(@PathVariable("id") long id) {
        Tag tag = new Tag();
        tag.setId(id);
        tagService.delete(tag);
        return showAll();
    }

    @PostMapping("")
    public List<Tag> create(@ModelAttribute("tag") Tag tag,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return null;

        tagService.create(tag);
        return showAll();
    }
}
