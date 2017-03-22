package com.caveat;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lszhen on 2017/3/22.
 */
public class Category {
    private String name;
    private Category parentCategory;
    private Set childCategories = new HashSet();

    public Category(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Set getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(Set childCategories) {
        this.childCategories = childCategories;
    }

}
