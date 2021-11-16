package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkAdder;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.service.dto.certificate.CertificateDtoService;
import com.epam.esm.service.dto.certificate.CertificateDtoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Validated
@RestController
@RequestMapping("/certificates")
public class CertificateController implements LinkAdder {

    private CertificateDtoServiceI service;

    @Autowired
    public CertificateController(CertificateDtoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CertificateDto> getAll(
            @RequestParam(defaultValue = "", required = false) String name,
            @RequestParam(defaultValue = "", required = false) String description,
            @RequestParam(defaultValue = "", required = false) String sortField,
            @RequestParam(defaultValue = "", required = false) String sortOrder,
            @RequestParam(defaultValue = "", required = false) String tagName,
            @Min(1) @RequestParam("page") int page,
            @Min(1) @RequestParam("size") int size
    ) {
        List<CertificateDto> certificates =
                service.findPaginated(name, description, sortField,
                        sortOrder, tagName, page, size);
        certificates.stream().forEach((certificateDto -> setLinks(certificateDto)));
        Link selfLink =
                linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                        .getAll(name, description, sortField, sortOrder, tagName, page, size)).withSelfRel();
        return certificates;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDto create(@Validated @RequestBody CertificateDto certificate) {
        CertificateDto certificateDto = service.create(certificate);
        setLinks(certificateDto);
        return certificateDto;
    }

    @GetMapping("/{id}")
    public CertificateDto getById(@Min(1) @PathVariable("id") long id) {
        CertificateDto certificateDto = service.read(id);
        setLinks(certificateDto);
        return certificateDto;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto update(@Min(1) @PathVariable Long id,
                                 @Validated @RequestBody CertificateDto patchedCertificate) {
        service.patch(id, patchedCertificate);
        CertificateDto certificateDto = service.read(id);
        setLinks(certificateDto);
        return certificateDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") @Min(1) int id) {
        service.delete(service.read(id));
    }

    private void setLinks(CertificateDto certificateDto) {
        addLinks(certificateDto);
        certificateDto.getTags().stream().forEach((tagDto -> addLinks(tagDto)));
    }
}