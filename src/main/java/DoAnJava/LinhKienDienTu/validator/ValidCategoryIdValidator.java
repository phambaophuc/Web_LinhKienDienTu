package DoAnJava.LinhKienDienTu.validator;

import DoAnJava.LinhKienDienTu.entity.Category;
import DoAnJava.LinhKienDienTu.validator.annotation.ValidCategoryId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCategoryIdValidator implements ConstraintValidator<ValidCategoryId, Category> {
    @Override
    public boolean isValid(Category category, ConstraintValidatorContext context) {
        return category != null && category.getCategoryId() != null;
    }
}
