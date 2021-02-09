import getBrowserLocale from "./i18n/get-user-locale";

export function formatDateString(date) {
    const opt = {
        timeZone: "UTC",
    };

    return date
        ? new Date(date).toLocaleDateString(getBrowserLocale(), opt)
        : "";
}

export function formatDateTimeString(date) {
    const opt = {
        timeZone: "UTC",
    };

    if (!date) {
        return "";
    }

    const dateToFormat = new Date(date);

    return `${formatDateString(date)} ${dateToFormat.toLocaleTimeString(getBrowserLocale(), opt)}`;
}