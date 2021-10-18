package com.epam.esm.mapper.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.dto.TagDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CertificateDtoMapperImplementation implements CertificateDtoMapper {
    @Override
    public Certificate certificateDtoToCertificate(CertificateDto certificateDto) {
        if (certificateDto == null) {
            return null;
        }
        Certificate certificate = new Certificate();
        certificate.setName(certificateDto.getName());
        certificate.setDescription(certificateDto.getDescription());
        certificate.setPrice(certificateDto.getPrice());
        certificate.setDuration(certificateDto.getDuration());
        certificate.setTags(tagDtoListToTagList(certificateDto.getTags()));
        certificate.setId(certificateDto.getId());
        return certificate;
    }

    @Override
    public CertificateDto certificateToCertificateDto(Certificate certificate) {
        if (certificate == null) {
            return null;
        }
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setName(certificate.getName());
        certificateDto.setDescription(certificate.getDescription());
        certificateDto.setPrice(certificate.getPrice());
        certificateDto.setDuration(certificate.getDuration());
        certificateDto.setTags(tagListToTagDtoList(certificate.getTags()));
        certificateDto.setId(certificate.getId());
        return certificateDto;
    }

    protected Tag tagDtoToTag(TagDto tagDto) {
        if (tagDto == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        tag.setId(tagDto.getId());
        return tag;
    }

    protected List<Tag> tagDtoListToTagList(List<TagDto> list) {
        if (list == null) {
            return null;
        }
        List<Tag> list1 = new ArrayList<Tag>(list.size());
        for (TagDto tagDto : list) {
            list1.add(tagDtoToTag(tagDto));
        }
        return list1;
    }

    protected TagDto tagToTagDto(Tag tag) {
        if (tag == null) {
            return null;
        }
        TagDto tagDto = new TagDto();
        tagDto.setName(tag.getName());
        tagDto.setId(tag.getId());
        return tagDto;
    }

    protected List<TagDto> tagListToTagDtoList(List<Tag> list) {
        if (list == null) {
            return null;
        }
        List<TagDto> list1 = new ArrayList<TagDto>(list.size());
        for (Tag tag : list) {
            list1.add(tagToTagDto(tag));
        }
        return list1;
    }
}
