package org.team4u.test;

import cn.hutool.json.JSONUtil;
import org.junit.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

public class MapStructTest {

    @Test
    public void entityToDTO() {
        PeopleDTO dto = PeopleMapper.INSTANCE.entityToDTO(new PeopleEntity()
                .setAddress("1")
                .setAge(2)
                .setEmile("3")
                .setCallNumber("4")
                .setName("5")
        );

        System.out.println(JSONUtil.toJsonPrettyStr(dto));

        PeopleEntity e = new PeopleEntity().setAddress("x");
        PeopleMapper.INSTANCE.updateEntityFromDto(dto, e);
        System.out.println(JSONUtil.toJsonPrettyStr(e));
    }

    @Mapper
    public interface PeopleMapper {
        PeopleMapper INSTANCE = Mappers.getMapper(PeopleMapper.class);

        @Mapping(target = "phoneNumber", source = "callNumber")
        @Mapping(target = "user.name", source = "name")
        @Mapping(target = "user.age", source = "age")
        PeopleDTO entityToDTO(PeopleEntity entity);

        @Mapping(target = "callNumber", source = "phoneNumber")
        @Mapping(target = "name", source = "user.name")
        @Mapping(target = "age", source = "user.age")
        void updateEntityFromDto(PeopleDTO peopleDTO, @MappingTarget PeopleEntity entity);
    }

    public static class User {
        private Integer age;
        private String name;

        public Integer getAge() {
            return age;
        }

        public User setAge(Integer age) {
            this.age = age;
            return this;
        }

        public String getName() {
            return name;
        }

        public User setName(String name) {
            this.name = name;
            return this;
        }
    }

    public static class PeopleDTO {
        private String phoneNumber;
        private String address;
        private String emile;
        private User user;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public PeopleDTO setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public String getAddress() {
            return address;
        }

        public PeopleDTO setAddress(String address) {
            this.address = address;
            return this;
        }

        public String getEmile() {
            return emile;
        }

        public PeopleDTO setEmile(String emile) {
            this.emile = emile;
            return this;
        }

        public User getUser() {
            return user;
        }

        public PeopleDTO setUser(User user) {
            this.user = user;
            return this;
        }
    }

    public static class PeopleEntity {
        private Integer age;
        private String name;
        private String callNumber;
        private String address;
        private String emile;

        public Integer getAge() {
            return age;
        }

        public PeopleEntity setAge(Integer age) {
            this.age = age;
            return this;
        }

        public String getName() {
            return name;
        }

        public PeopleEntity setName(String name) {
            this.name = name;
            return this;
        }

        public String getCallNumber() {
            return callNumber;
        }

        public PeopleEntity setCallNumber(String callNumber) {
            this.callNumber = callNumber;
            return this;
        }

        public String getAddress() {
            return address;
        }

        public PeopleEntity setAddress(String address) {
            this.address = address;
            return this;
        }

        public String getEmile() {
            return emile;
        }

        public PeopleEntity setEmile(String emile) {
            this.emile = emile;
            return this;
        }
    }
}