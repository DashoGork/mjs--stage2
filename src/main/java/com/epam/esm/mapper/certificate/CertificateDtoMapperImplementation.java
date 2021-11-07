package com.epam.esm.mapper.certificate;

import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

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

    protected Set<Tag> tagDtoListToTagList(Set<TagDto> list) {
        if (list == null) {
            return null;
        }
        Set<Tag> list1 = new HashSet<>(list.size());
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

    protected Set<TagDto> tagListToTagDtoList(Set<Tag> list) {
        if (list == null) {
            return null;
        }
        Set<TagDto> list1 = new HashSet<>(list.size());
        for (Tag tag : list) {
            list1.add(tagToTagDto(tag));
        }
        return list1;
    }
}
