package com.epam.esm.service.entity.tag;

import com.epam.esm.dao.order.OrderDao;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.entity.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class TagService implements TagServiceI, PaginationService<Tag> {
    private TagDao tagDao;
    private OrderDao orderDao;

    @Autowired
    public TagService(TagDao tagDao,
                      OrderDao orderDao) {
        this.tagDao = tagDao;
        this.orderDao = orderDao;
    }


    @Transactional
    public Tag create(Tag tag) {
        try {
            return read(tag.getName());
        } catch (TagNotFoundException e) {
            return tagDao.save(tag);
        }
    }

    @Transactional
    public void delete(Tag tag) {
        tagDao.delete(tag);
    }

    public List<Tag> read() {
        return tagDao.findAll();
    }

    @Override
    public List<Tag> findPaginated(int size, int page) {
        return paginate(read(), size, page);
    }

    public Tag read(String name) {
        if (name != null) {
            Optional<Tag> tag = Optional.ofNullable(tagDao.findTagByName(name));
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
        if (id > 0) {
            Optional<Tag> tag = tagDao.findById(id);
            if (!tag.isPresent()) {
                throw new TagNotFoundException("Tag wasn't" +
                        " found. id =" + id);
            } else {
                return tag.get();
            }
        } else {
            throw new InvalidParameterException("invalid id. id = " + id);
        }
    }

    @Override
    public Tag getTagsOfUserWithHighestPriceOfOrders() {
        List<Tag> mostUsedTagOfTopUser =
                tagDao.findMostUsedTagOfTopUser(orderDao.findTopUserByPrice().get(0));
        return mostUsedTagOfTopUser.get(0);
    }
}