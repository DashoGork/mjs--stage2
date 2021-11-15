package com.epam.esm.service.entity.tag;

import com.epam.esm.dao.tag.TagDaoI;
import com.epam.esm.dao.tag.impl.TagDao;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.entity.PaginationCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TagService implements TagServiceI, PaginationCalcService {
    private TagDaoI tagDaoI;

    @Autowired
    public TagService(
            TagDao tagDaoI) {
        this.tagDaoI = tagDaoI;
    }


    @Transactional
    public Tag create(Tag tag) {
        try {
            return read(tag.getName());
        } catch (TagNotFoundException e) {
            tagDaoI.create(tag);
            return read(tag.getName());
        }
    }

    @Transactional
    public void delete(Tag tag) {
        tagDaoI.delete(tag);
    }

    public List<Tag> read() {
        return tagDaoI.read();
    }

    @Override
    public List<Tag> findPaginated(int size, int page) {
        Map<String, Integer> indexes = paginate(read().size(), size, page);
        return tagDaoI.read(indexes.get("offset"), indexes.get("limit"));
    }

    public Tag read(String name) {
        if (name != null) {
            Optional<Tag> tag = (tagDaoI.findTagByName(name));
            if (!tag.isPresent()) {
                throw new TagNotFoundException("Tag wasn't" +
                        " found. name =" + name);
            } else {
                return tag.get();
            }
        } else {
            throw new InvalidParameterException("name is null");
        }
    }

    public Tag read(long id) {
        Optional<Tag> tag = tagDaoI.read(id);
        if (!tag.isPresent()) {
            throw new TagNotFoundException("Tag wasn't" +
                    " found. id =" + id);
        } else {
            return tag.get();
        }
    }

    @Override
    public Tag getTagsOfUserWithHighestPriceOfOrders() {
        String mostUsedTagOfTopUser =
                tagDaoI.findMostUsedTagOfTopUser();
        return read(mostUsedTagOfTopUser);
    }
}