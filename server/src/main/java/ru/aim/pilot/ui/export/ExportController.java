package ru.aim.pilot.ui.export;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.aim.pilot.model.Revision;
import ru.aim.pilot.model.RevisionType;
import ru.aim.pilot.repository.RevisionRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Controller
public class ExportController {

    private final RevisionRepository revisionRepository;

    private final ExcelExportService excelExportService;

    @Autowired
    public ExportController(RevisionRepository revisionRepository, ExcelExportService excelExportService) {
        this.revisionRepository = revisionRepository;
        this.excelExportService = excelExportService;
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    void export(@RequestParam("terId") Long terId, @RequestParam("revType") int revType, HttpServletResponse response) throws Exception {
        RevisionType revisionType = RevisionType.values()[revType];
        List<Revision> list = revisionRepository.findByTerritoryIdAndType(terId, revisionType);
        File file = excelExportService.export(list);
        InputStream inputStream = new FileInputStream(file);
        response.setContentType(ExcelExportService.EXCEL_MIME_TYPE);
        IOUtils.copy(inputStream, response.getOutputStream());
    }
}
