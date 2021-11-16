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
        certificate.setId(certificateDto.getId());
        certificate.setName(certificateDto.getName());
        certificate.setDescription(certificateDto.getDescription());
        certificate.setPrice(certificateDto.getPrice());
        certificate.setDuration(certificateDto.getDuration());
        certificate.setCreateDate(certificateDto.getCreateDate());
        certificate.setLastUpdateDate(certificateDto.getLastUpdateDate());
        certificate.setTags(tagDtoSetToTagSet(certificateDto.getTags()));
        return certificate;
    }

    @Override
    public CertificateDto certificateToCertificateDto(Certificate certificate) {
        if (certificate == null) {
            return null;
        }
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setId(certificate.getId());
        certificateDto.setDescription(certificate.getDescription());
        certificateDto.setName(certificate.getName());
        certificateDto.setPrice(certificate.getPrice());
        certificateDto.setDuration(certificate.getDuration());
        certificateDto.setCreateDate(certificate.getCreateDate());
        certificateDto.setLastUpdateDate(certificate.getLastUpdateDate());
        certificateDto.setTags(tagSetToTagDtoSet(certificate.getTags()));
        return certificateDto;
    }

    protected Tag tagDtoToTag(TagDto tagDto) {
        if (tagDto == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setId(tagDto.getId());
        tag.setName(tagDto.getName());
        return tag;
    }

    protected Set<Tag> tagDtoSetToTagSet(Set<TagDto> set) {
        if (set == null) {
            return null;
        }
        Set<Tag> set1 = new HashSet<Tag>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (TagDto tagDto : set) {
            set1.add(tagDtoToTag(tagDto));
        }
        return set1;
    }

    protected TagDto tagToTagDto(Tag tag) {
        if (tag == null) {
            return null;
        }
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    protected Set<TagDto> tagSetToTagDtoSet(Set<Tag> set) {
        if (set == null) {
            return null;
        }
        Set<TagDto> set1 = new HashSet<TagDto>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (Tag tag : set) {
            set1.add(tagToTagDto(tag));
        }
        return set1;
    }
}
