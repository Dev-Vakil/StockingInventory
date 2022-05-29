package com.example.stockinginventory;

public class ItemNote {
    private String ItemId;
    private String ItemInfo;
    private int ItemAmount;
    private int TempItemAmount;

    public ItemNote(){
        //no args here
    }

    public ItemNote(String ItemId, String ItemInfo, int ItemAmount, int TempItemAmount){
        this.ItemId = ItemId;
        this.ItemInfo = ItemInfo;
        this.ItemAmount = ItemAmount;
        this.TempItemAmount = TempItemAmount;
    }

    public int getTempItemAmount() {
        return TempItemAmount;
    }

    public void setTempItemAmount(int tempItemAmount) {
        TempItemAmount = tempItemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.ItemAmount = itemAmount;
    }

    public int getItemAmount() {
        return ItemAmount;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public void setItemInfo(String itemInfo) {
        ItemInfo = itemInfo;
    }

    public String getItemInfo() {
        return ItemInfo;
    }

}
