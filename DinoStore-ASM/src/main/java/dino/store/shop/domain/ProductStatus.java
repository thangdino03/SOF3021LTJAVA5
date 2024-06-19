package dino.store.shop.domain;

public enum ProductStatus {
	ACTIVE((short) 1),
    INACTIVE((short) 2),
    OUT_OF_STOCK((short) 3);

    private final short value;

    ProductStatus(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
