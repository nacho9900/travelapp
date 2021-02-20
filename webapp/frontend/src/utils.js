import getBrowserLocale from "./i18n/get-user-locale";

export function formatDateString(date) {
    if (!date) {
        return "";
    }

    const dateObj =  new Date(new Date(date).getTime() + new Date().getTimezoneOffset() * 60000); 

    return dateObj.toLocaleDateString(getBrowserLocale);
}

export function formatDateTimeString(date) {
    if (!date) {
        return "";
    }

    const dateToFormat = new Date(date + "+0000");

    return `${dateToFormat.toLocaleString(getBrowserLocale())}`;
}