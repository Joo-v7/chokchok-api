package com.chokchok.chokchokapi.category.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "categories")
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer depth;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    private Category(String name, Category parent, Integer depth) {
        this.name = name;
        this.parent = parent;
        this.depth = depth;
    }

    /**
     * 카테고리를 생성하는 정적 팩토리 메소드입니다.
     * @param name
     * @param parent
     * @return Category
     */
    public static Category create(String name, Category parent) {
        Integer depth = (parent == null) ? 0 : parent.getDepth() + 1;
        return new Category(name, parent, depth);
    }

    /**
     * 자식 카테고리를 추가합니다.
     * @param child
     */
    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * 자식 카테고리를 삭제합니다.
     * @param child
     */
    public void removeChild(Category child) {
        children.remove(child);
        child.setParent(null);
    }

    /**
     * 카테고리 이름을 수정합니다.
     * @param name
     */
    public void updateName(String name) {
        this.name = name;
    }

    /**
     * 카테고리 깊이를 수정합니다.
     * @param depth
     */
    public void updateDepth(Integer depth) {
        this.depth = depth;
    }

    /**
     * 부모 카테고리를 수정합니다.
     * @param parent
     */
    public void updateParent(Category parent) {
        this.parent = parent;
    }

    /**
     * 카테고리 soft deleted를 위해 isDeleted를 true로 바꿉니다.
     */
    public void delete() {
        this.isDeleted = true;
    }

}
