package com.atharva.educationcrm.dto;

import com.atharva.educationcrm.entity.Payment.PaymentMethod;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public class CheckoutForm {

    @NotNull
    private Long courseId;

    @NotNull(message = "Choose a payment method")
    private PaymentMethod paymentMethod;

    @AssertTrue(message = "Confirm the demo purchase")
    private boolean confirmed;

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public boolean isConfirmed() { return confirmed; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
}
