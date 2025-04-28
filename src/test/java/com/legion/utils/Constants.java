package com.legion.utils;

public class Constants {
    public static final String NoLongEligibleTakeShiftErrorMessage = "Error! We are sorry. You are not eligible to claim this shift.";
    public static final String WillTriggerWeeklyOTErrorMessage = "Error!We are sorry. You are not eligible to claim this shift, as it will trigger Over Time for the week.";
    public static final String WillTriggerDailyOTErrorMessage = "Error!We are sorry. You are not eligible to claim this shift, as it will trigger Over Time for the day.";
    public static final String ClaimSuccessMessage = "Success! This shift is yours, and has been added to your schedule.";
    public static final String ClaimRequestBeenSendForApprovalMessage = "Your claim request has been received and sent for approval";
    public static final String Hr = "HR";
    public static final String Ftp = "FTP";
    public static final String Custom = "CUSTOM";
    public static final String Enabled = "ENABLED";
    public static final String Disabled = "DISABLED";
    public static final String Utc = "UTC";
    public static final String Terminate = "Terminate";
    public static final String Activate = "Activate";
    public static final String Transfer = "Transfer";
    public static final String Deactivate = "Deactivate";
    public static final String SendUsername = "Send Username";
    public static final String ResetPassword = "Reset Password";
    public static final String ChangePassword = "Change Password";
    public static final String ManualOnboard = "Manual Onboard";
    public static final String ControlEnterprice = "Vailqacn_Enterprise";
    public static final String OpEnterprise1 = "CinemarkWkdy_Enterprise";
    public static final String OpEnterprise2 = "Circlek_Enterprise";

    //accrual
    public static final String loginUrlRC =System.getProperty("env")+"legion/authentication/login";
    public static final String getTemplateByWorkerId =System.getProperty("env")+"legion/configTemplate/getTemplateByWorkerAndType";
    public static final String deleteAccrualByWorkerId =System.getProperty("env")+"legion/accrual/deleteAccrualByWorkerId";
    public static final String runAccrualJobWithSimulateDateAndWorkerId=System.getProperty("env")+"legion/accrual/runAccrualJobWithSimulateDateAndWorkerIdAndZone";
    public static final String toggles =System.getProperty("env")+"legion/toggles";
    public static final String getHoliday = System.getProperty("env")+"legion/metadata/getHolidays";
    public static final String getTimeOffBalance = System.getProperty("env")+"legion/api/accruals";

    //downloadTranslation
    public static final String downloadTransation1 =System.getProperty("env")+"legion/translation/downloadTranslations";

    //accessRole
    public static final String uploadUserAccessRole =System.getProperty("env")+"legion/integration/uploadUserAccessRole";

    //uploadTranslation
    public static final String uploadTransation =System.getProperty("env")+"legion/translation/uploadTranslations";

    //copyWorkRolesFromControlsToOP
    public static final String copyWorkRole =System.getProperty("env")+"legion/configTemplate/copyWorkRolesFromControlsToOP";

    //uploadFiscalCalendar
    public static final String uploadFiscalCalendar = System.getProperty("env")+"legion/fiscalCalendars/upload";

    //refreshCache
    public static final String refreshCache = System.getProperty("env")+"legion/cache/refreshCache";

    //import location
    public static final String uploadBusiness = System.getProperty("env")+"legion/integration/uploadBusiness";

    //download location
    public static final String downloadBusiness = System.getProperty("env")+"legion/integration/downloadBusiness";

    //employee attributes
    public static final String addEmployeeAttributes = System.getProperty("env")+"legion/v2/external-attributes/employee-attributes";

    //Upload Accrual Ledger Data
    public static final String uploadAccrualLedgerData = System.getProperty("env")+"legion/integration/testUploadAccrualLedgerData";

    //import location Attribute File
    public static final String uploadLocationAttributeFile = System.getProperty("env")+"legion/externalStore/testUploadLocationAttribute";

    //import location Attribute API
    public static final String importLocationAttribute = System.getProperty("env")+"legion/v2/external-attributes/location-attributes";
}
