package com.epam.esm.mapper.user;

import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserDtoMapperImpl implements UserDtoMapper {

    @Override
    public User userDtoToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setPurse(userDto.getPurse());
        user.setOrders(orderDtoListToOrderList(userDto.getOrders()));
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return user;
    }

    @Override
    public UserDto userToUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setPurse(user.getPurse());
        userDto.setOrders(orderListToOrderDtoList(user.getOrders()));
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
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

    protected Certificate certificateDtoToCertificate(CertificateDto certificateDto) {
        if (certificateDto == null) {
            return null;
        }
        Certificate certificate = new Certificate();
        certificate.setId(certificateDto.getId());
        certificate.setName(certificateDto.getName());
        certificate.setDescription(certificateDto.getDescription());
        certificate.setPrice(certificateDto.getPrice());
        certificate.setDuration(certificateDto.getDuration());
        certificate.setTags(tagDtoSetToTagSet(certificateDto.getTags()));
        return certificate;
    }

    protected Set<Certificate> certificateDtoSetToCertificateSet(Set<CertificateDto> set) {
        if (set == null) {
            return null;
        }
        Set<Certificate> set1 = new HashSet<Certificate>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (CertificateDto certificateDto : set) {
            set1.add(certificateDtoToCertificate(certificateDto));
        }
        return set1;
    }

    protected Order orderDtoToOrder(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setPrice(orderDto.getPrice());
        order.setUserId(orderDto.getUserId());
        order.setCertificates(certificateDtoSetToCertificateSet(orderDto.getCertificates()));
        return order;
    }

    protected List<Order> orderDtoListToOrderList(List<OrderDto> list) {
        if (list == null) {
            return null;
        }
        List<Order> list1 = new ArrayList<Order>(list.size());
        for (OrderDto orderDto : list) {
            list1.add(orderDtoToOrder(orderDto));
        }
        return list1;
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

    protected CertificateDto certificateToCertificateDto(Certificate certificate) {
        if (certificate == null) {
            return null;
        }
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setId(certificate.getId());
        certificateDto.setPrice(certificate.getPrice());
        certificateDto.setDuration(certificate.getDuration());
        certificateDto.setDescription(certificate.getDescription());
        certificateDto.setName(certificate.getName());
        certificateDto.setTags(tagSetToTagDtoSet(certificate.getTags()));
        return certificateDto;
    }

    protected Set<CertificateDto> certificateSetToCertificateDtoSet(Set<Certificate> set) {
        if (set == null) {
            return null;
        }
        Set<CertificateDto> set1 = new HashSet<CertificateDto>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (Certificate certificate : set) {
            set1.add(certificateToCertificateDto(certificate));
        }
        return set1;
    }

    protected OrderDto orderToOrderDto(Order order) {
        if (order == null) {
            return null;
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setPrice(order.getPrice());
        orderDto.setUserId(order.getUserId());
        orderDto.setTimeOfPurchase(order.getTimeOfPurchase());
        orderDto.setCertificates(certificateSetToCertificateDtoSet(order.getCertificates()));
        return orderDto;
    }

    protected List<OrderDto> orderListToOrderDtoList(List<Order> list) {
        if (list == null) {
            return null;
        }
        List<OrderDto> list1 = new ArrayList<OrderDto>(list.size());
        for (Order order : list) {
            list1.add(orderToOrderDto(order));
        }
        return list1;
    }
}