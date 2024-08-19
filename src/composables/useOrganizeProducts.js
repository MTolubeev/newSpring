
export const useOrganizeProducts = () => {
  const organizeProductsByCategories = (products) => {
    const categoryMap = new Map();

    products.forEach(product => {
      product.categories.forEach(cat => {
        let category = categoryMap.get(cat.name);
        if (!category) {
          category = { name: cat.name, subcategories: [], productsWithoutSubcategory: [] };
          categoryMap.set(cat.name, category);
        }

        if (!cat.subcategory || cat.subcategory === '') {
          category.productsWithoutSubcategory.push(product);
        } else {
          let subcategory = category.subcategories.find(sub => sub.name === cat.subcategory);
          if (!subcategory) {
            subcategory = { name: cat.subcategory, products: [], subsubcategories: [] };
            category.subcategories.push(subcategory);
          }

          if (cat.subsubcategory) {
            let subsubcategory = subcategory.subsubcategories.find(subsub => subsub.name === cat.subsubcategory);
            if (!subsubcategory) {
              subsubcategory = { name: cat.subsubcategory, products: [] };
              subcategory.subsubcategories.push(subsubcategory);
            }
            subsubcategory.products.push(product);
          } else {
            subcategory.products.push(product);
          }
        }
      });
    });

    return Array.from(categoryMap.values());
  };

  return {
    organizeProductsByCategories
  };
};