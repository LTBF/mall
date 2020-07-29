package com.imooc.mall.service.impl;

import com.imooc.mall.dao.CategoryMapper;
import com.imooc.mall.pojo.Category;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.imooc.mall.consts.MallConst.ROOT_PARENT_ID;

/**
 * @author shkstart
 * @create 2020-04-03 15:55
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    // 查询所有产品，writebyself
    /*
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        List<CategoryVo> categoryList = new ArrayList<>();

        List<CategoryVo> curCategoryList, nextCategoryList;
        List<Category> categories = categoryMapper.selectAll();

        //查出parent_id = 0
        for(Category category : categories){
            if(category.getParentId() == ROOT_PARENT_ID){
                CategoryVo categoryVo = new CategoryVo();
                BeanUtils.copyProperties(category, categoryVo);
                categoryList.add(categoryVo);
            }
        }

        curCategoryList = categoryList;
        while(!curCategoryList.isEmpty()){
            nextCategoryList = new ArrayList<>();
            for(CategoryVo parent: curCategoryList){
                for(Category category : categories){
                    if(parent.getId().equals(category.getParentId())){
                        if(parent.getSubCategories() == null)
                            parent.setSubCategories(new ArrayList<>());
                        CategoryVo categoryVo = new CategoryVo();
                        BeanUtils.copyProperties(category, categoryVo);
                        parent.getSubCategories().add(categoryVo);
                        nextCategoryList.add(categoryVo);
                    }
                }
            }
            curCategoryList = nextCategoryList;
        }


        return ResponseVo.success(categoryList);
    }
    */

    public ResponseVo<List<CategoryVo>> selectAll() {
        List<Category> categories = categoryMapper.selectAll();

        //查出parent_id = 0, lambda+ stream
        List<CategoryVo> categoryList = categories.stream()
                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID))
                .map(this::category2CategoryVo)
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                .collect(Collectors.toList());

        // 查询子目录
        findSubCategory(categoryList, categories);
        return ResponseVo.success(categoryList);
    }

    // 递归实现
    public void findSubCategory(List<CategoryVo> categoryList, List<Category> categories){
        for(CategoryVo categoryVo : categoryList){
            List<CategoryVo> curList = new ArrayList<>();
            for(Category category : categories){
                if(categoryVo.getId().equals(category.getParentId())){
                    CategoryVo cv = category2CategoryVo(category);
                    curList.add(cv);
                }
            }
            curList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed()); // 根据sort_order排序
            categoryVo.setSubCategories(curList);
            findSubCategory(curList, categories);
        }
    }

    public CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id, resultSet, categories);
    }

    public void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for(Category category : categories){
            if(category.getParentId().equals(id)){
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }
}
