package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkAdder;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.dto.tag.TagDtoService;
import com.epam.esm.service.dto.tag.TagDtoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping("/tags")
public class TagController implements LinkAdder {
    private final TagDtoServiceI tagService;

    @Autowired
    public TagController(TagDtoService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public CollectionModel<TagDto> getAll(@RequestParam("page") int page,
                                          @RequestParam("size") int size) {
        final List<TagDto> tags = tagService.findPaginated(page, size);
        for (final TagDto tag : tags) {
            addLinks(tag);
        }
        Link link =
                linkTo(WebMvcLinkBuilder.methodOn(TagController.class).getAll(page, size)).withSelfRel();
        CollectionModel<TagDto> result = CollectionModel.of(tags
                , link);
        return result;
    }

    @GetMapping("/{id}")
    public TagDto getById(@PathVariable("id") long id) {
        TagDto tagDto = tagService.read(id);
        addLinks(tagDto);
        return tagDto;
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
        TagDto tagDto = tagService.create(tag);
        addLinks(tagDto);
        return tagDto;
    }
}