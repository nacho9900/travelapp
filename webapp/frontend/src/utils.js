import getBrowserLocale from "./i18n/get-user-locale";

export function formatDateString(date) {
    return date
        ? new Date(date).toLocaleDateString(getBrowserLocale())
        : "";
}

export function formatDateTimeString(date) {
    if (!date) {
        return "";
    }

    const dateToFormat = new Date(date + "+0000");

    return `${dateToFormat.toLocaleString(getBrowserLocale())}`;
}