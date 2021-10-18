package com.epam.esm.controller;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.dto.tag.TagDtoService;
import com.epam.esm.service.dto.tag.TagDtoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagDtoServiceI tagService;

    @Autowired
    public TagController(TagDtoService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getAll() {
        List<TagDto> gg = tagService.read();
        return gg;
    }

    @GetMapping("/{id}")
    public TagDto getById(@PathVariable("id") int id) {
        return tagService.read(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") long id) {
        TagDto tag = new TagDto();
        tag.setId(id);
        tagService.delete(tag);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@Validated @RequestBody TagDto tag) {
        tagService.create(tag);
        return tagService.read(tag.getName());
    }
}