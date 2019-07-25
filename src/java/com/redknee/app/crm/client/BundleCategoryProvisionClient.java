package com.redknee.app.crm.client;

import com.redknee.product.bundle.manager.provision.v5_0.category.Category;


public interface BundleCategoryProvisionClient
{
    public short createCategory(Category category);
    public short updateCategory(int spId, int categoryId, Category category);
    public short removeCategory(int spId, int categoryId);
    public Category getBundleCategory(int spId, int categoryId);
}
