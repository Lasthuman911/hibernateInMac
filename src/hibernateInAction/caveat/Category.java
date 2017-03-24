package hibernateInAction.caveat;

import java.util.HashSet;
import java.util.Set;

/**
 * Name: admin
 * Date: 2017/3/23
 * Time: 11:10
 */
public class Category {
    private String name;
    private Category parentCategory;
    private Set childCategories = new HashSet();
    private Set items = new HashSet();

    public Set getItems() {
        return items;
    }

    public void setItems(Set items) {
        this.items = items;
    }

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
    //保证数据的完整性
    public void addChildCategory(Category childCategory){
        if (childCategory == null)
            throw new IllegalArgumentException("Null child category!");
        if (childCategory.getParentCategory() !=null)
            childCategory.getParentCategory().getChildCategories().remove(childCategory);
        childCategory.setParentCategory(this);
        childCategories.add(childCategory);

    }

}
