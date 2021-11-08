package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkAdder;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.dto.tag.TagDtoService;
import com.epam.esm.service.dto.tag.TagDtoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping("/tags")
public class TagController implements LinkAdder {
    private final TagDtoServiceI tagService;

    @Autowired
    public TagController(TagDtoService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getAll(@Min(1) @RequestParam("page") int page,
                               @Min(1) @RequestParam("size") int size) {
        final List<TagDto> tags = tagService.findPaginated(page, size);
        for (final TagDto tag : tags) {
            addLinks(tag);
        }
        return tags;
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

    @GetMapping("/best")
    public TagDto getMostUsed() {
        TagDto tagDto = tagService.getTagsOfUserWithHighestPriceOfOrders();
        addLinks(tagDto);
        return tagDto;
    }
}