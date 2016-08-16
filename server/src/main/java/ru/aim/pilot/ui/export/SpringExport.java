package ru.aim.pilot.ui.export;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
