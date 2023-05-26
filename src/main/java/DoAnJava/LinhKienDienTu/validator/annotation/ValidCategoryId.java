package DoAnJava.LinhKienDienTu.validator.annotation;

import DoAnJava.LinhKienDienTu.validator.ValidCategoryIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCategoryIdValidator.class)
@Documented
public @interface ValidCategoryId {
    String message() default "Vui lòng chọn loại sản phẩm.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
