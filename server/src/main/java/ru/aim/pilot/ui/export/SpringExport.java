package ru.aim.pilot.ui.export;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Table;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class SpringExport extends ExcelExport {

    private File tempFile;

    public SpringExport(Table table) {
        super(table);
    }

    @Override
    public void convertTable() {
        super.convertTable();
    }

    @Override
    public boolean sendConverted() {
        tempFile = null;
        FileOutputStream fileOut = null;

        try {
            tempFile = File.createTempFile("tmp", ".xls");
            fileOut = new FileOutputStream(tempFile);
            this.workbook.write(fileOut);
            if (null == this.mimeType) {
                this.setMimeType(EXCEL_MIME_TYPE);
            }
            return true;
        } catch (IOException ignored) {
        } finally {
            try {
                fileOut.close();
            } catch (IOException ignored) {
            }

        }
        return false;
    }

    public File getTempFile() {
        return tempFile;
    }

    protected void addTotalsRow(int currentRow, int startRow) {
        this.totalsRow = this.sheet.createRow(currentRow);
        this.totalsRow.setHeightInPoints(30.0F);

        for (int col = 0; col < this.getPropIds().size(); ++col) {
            Object propId = this.getPropIds().get(col);
            Cell cell = this.totalsRow.createCell(col);
            cell.setCellStyle(this.getCellStyle(Integer.valueOf(currentRow), startRow, col, true));
            Short poiAlignment = this.getTableHolder().getCellAlignment(propId);
            CellUtil.setAlignment(cell, this.workbook, poiAlignment.shortValue());
            Class propType = this.getPropertyType(propId);
            if (this.isNumeric(propType)) {
                CellRangeAddress cra = new CellRangeAddress(startRow, currentRow - 1, col, col);
                if (this.isHierarchical()) {
                    cell.setCellFormula("SUM(" + cra.formatAsString(this.hierarchicalTotalsSheet.getSheetName(), true) + ")");
                } else {
                    cell.setCellFormula("SUM(" + cra.formatAsString() + ")");
                }
            } else if (0 == col) {
                cell.setCellValue(this.createHelper.createRichTextString("Итого"));
            }
        }

    }

    private boolean isNumeric(Class<?> type) {
        return this.isIntegerLongShortOrBigDecimal(type) || this.isDoubleOrFloat(type);
    }

    private boolean isIntegerLongShortOrBigDecimal(Class<?> type) {
        return !(!Integer.class.equals(type) && !Integer.TYPE.equals(type)) || (!Long.class.equals(type) && !Long.TYPE.equals(type) ? (!Short.class.equals(type) && !Short.TYPE.equals(type) ? BigDecimal.class.equals(type) || BigDecimal.class.equals(type) : true) : true);
    }

    private boolean isDoubleOrFloat(Class<?> type) {
        return !(!Double.class.equals(type) && !Double.TYPE.equals(type)) || (Float.class.equals(type) || Float.TYPE.equals(type));
    }

    private Class<?> getPropertyType(Object propId) {
        Class classType;
        if (this.getTableHolder().isGeneratedColumn(propId)) {
            classType = this.getTableHolder().getPropertyTypeForGeneratedColumn(propId);
        } else {
            classType = this.getTableHolder().getContainerDataSource().getType(propId);
        }

        return classType;
    }

}
