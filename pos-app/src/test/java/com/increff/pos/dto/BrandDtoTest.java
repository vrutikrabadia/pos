package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestUtil;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidateUtil;

public class BrandDtoTest extends AbstractUnitTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Autowired
    private BrandDto dto;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void testAddAndGet() throws ApiException {
        String brand = "Brand1 ";
        String category = " caTegory1";

        BrandForm form = testUtil.getBrandForm(brand, category);

        dto.add(form);

        List<BrandData> d = dto.getAll(0, 1, 1, testUtil.empty).getData();

        assertEquals(d.get(0).getBrand(), "brand1");
        assertEquals(d.get(0).getCategory(), "category1");
    }

    @Test
    public void testUpdate() throws ApiException {
        String brand = "brand1";
        String category = "category1";

        testUtil.addBrand(brand, category);
        BrandForm f = testUtil.getBrandForm(brand, category);

        f.setCategory("category2");
        f.setBrand("brand2");

        List<BrandData> d = dto.getAll(0, 1, 1, testUtil.empty).getData();

        dto.update(d.get(0).getId(), f);

        d = dto.getAll(0, 1, 1, testUtil.empty).getData();

        assertEquals(d.get(0).getBrand(), "brand2");
        assertEquals(d.get(0).getCategory(), "category2");

    }

    @Test
    public void testGetAll() throws ApiException {
        testUtil.addBrand("brand1", "category1");
        testUtil.addBrand("brand2", "category2");
        testUtil.addBrand("brand3", "category3");

        List<BrandData> d = dto.getAll(0, 5, 1, testUtil.empty).getData();

        assertEquals(d.size(), 3);
    }

    @Test
    public void testDupicateInsertion() throws ApiException {

        testUtil.addBrand("brand1", "category1");

        BrandForm form = testUtil.getBrandForm("brand1", "category1");

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand and category already exists");
        dto.add(form);
    }

    @Test
    public void testGetById() throws ApiException {
        String brand = "brand1";
        String category = "category1";

        testUtil.addBrand(brand, category);

        List<BrandData> d = dto.getAll(0, 1, 1, testUtil.empty).getData();

        BrandData d1 = dto.get(d.get(0).getId());

        assertEquals(d1.getBrand(), brand);
        assertEquals(d1.getCategory(), category);
    }

    @Test
    public void testGetByBrandCategory() throws ApiException {
        String brand = "brand1";
        String category = "category1";

        testUtil.addBrand(brand, category);

        BrandData d = dto.get(brand, category);

        if (d == null) {
            assertTrue(false);
        } else {
            assertEquals(d.getCategory(), category);
            assertEquals(d.getBrand(), brand);
        }
    }

    @Test
    public void testBrandValidationBrandNull() throws ApiException {
        List<BrandForm> brandList = new ArrayList<BrandForm>();

        brandList.add(testUtil.getBrandForm(null, "hello"));

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("[Brand cannot be blank]");
        ValidateUtil.validateList(brandList);

    }

    @Test
    public void testBrandValidationCategoryNull() throws ApiException {

        List<BrandForm> brandList = new ArrayList<BrandForm>();

        brandList.add(testUtil.getBrandForm("hello", null));
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("[Category cannot be blank]");
        ValidateUtil.validateList(brandList);

    }

    @Test(expected = ApiException.class)
    public void testFileDuplicate() throws ApiException {

        List<BrandForm> brandList = new ArrayList<BrandForm>();

        BrandForm f1 = testUtil.getBrandForm("b1", "c1");
        brandList.add(f1);

        BrandForm f2 = testUtil.getBrandForm("b2", "c2");
        brandList.add(f2);

        dto.checkFileDuplications(brandList);

        brandList.add(f2);

        dto.checkFileDuplications(brandList);

    }

    @Test(expected = ApiException.class)
    public void testDbDuplicate() throws ApiException {
        testUtil.addBrand("b2", "c2");

        List<BrandForm> brandList = new ArrayList<BrandForm>();

        BrandForm f1 = testUtil.getBrandForm("b1", "c1");
        brandList.add(f1);

        BrandForm f2 = testUtil.getBrandForm("b2", "c2");
        brandList.add(f2);

        List<BrandPojo> pojoList = brandList.stream().map(e -> ConvertUtil.objectMapper(e, BrandPojo.class))
                .collect(Collectors.toList());

        dto.checkDbDuplicate(pojoList);

    }

    @Test
    public void testBulkAdd() throws ApiException {
        List<BrandForm> brandList = new ArrayList<BrandForm>();

        BrandForm f1 = testUtil.getBrandForm("b1", "c1");
        brandList.add(f1);

        BrandForm f2 = testUtil.getBrandForm("b2", "c2");
        brandList.add(f2);

        dto.bulkAdd(brandList);

        SelectData<BrandData> dataList = dto.getAll(0, 3, 0, testUtil.empty);

        assertEquals(2, dataList.getData().size());
    }

    @Test(expected = ApiException.class)
    public void testUpdateAreadyExists() throws ApiException {
        testUtil.addBrand("b1", "c1");
        testUtil.addBrand("b2", "c2");

        List<BrandData> d = dto.getAll(0, 2, 0, testUtil.empty).getData();

        BrandForm f = testUtil.getBrandForm("b1", "c1");
        f.setBrand("b2");
        f.setCategory("c2");

        dto.update(d.get(0).getId(), f);
    }

    @Test
    public void testGetAllSearchValue() {
        testUtil.addBrand("b1", "c1");
        testUtil.addBrand("b2", "c2");
        testUtil.addBrand("b3", "c3");

        SelectData<BrandData> d = dto.getAll(0, 3, 0, Optional.of("b1"));

        assertEquals(1, d.getData().size());
    }

}
