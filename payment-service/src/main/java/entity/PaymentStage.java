package entity;

public enum PaymentStage {
    UNPAID("Unpaid"),
    WAITING_FOR_PAYMENT("Waiting for Payment"),
    PAID("Paid");

    private final String displayName;

    PaymentStage(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
