package service.interfaces;

import java.time.LocalDate;

public interface IReportManagement {

    void generateCurrentlyBorrowedReport();

    void generateOverdueReport(LocalDate checkDate);

    void generatePopularBooksReport();

    void generateTopMembersReport();
}
