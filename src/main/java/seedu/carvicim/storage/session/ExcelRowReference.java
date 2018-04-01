package seedu.carvicim.storage.session;

/**
 * For row entries of an excel sheet
 */
public interface ExcelRowReference {

    /**
     * Returns the excel sheet number of this element.
     */
    public int getSheetNumber();

    /**
     * Returns the excel row number of this element
     * @return
     */
    public int getRowNumber();
}
