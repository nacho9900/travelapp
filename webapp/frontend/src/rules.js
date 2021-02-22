import i18n from './i18n/i18n.js';

export const emailRules = [
    (v) =>
        !v || /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
            v
        ) || i18n.t("rules.email.format"),
];

export const passwordRules = [
    (v) => !!v || i18n.t("rules.password.required"),
    (v) => /(?=.*[A-Z])[a-zA-Z0-9]{8,20}/.test(v) || i18n.t("rules.password.format"),
];

export const requiredRule = [
    (v) => (!!v || v === 0) || i18n.t("rules.field_required")
];

export const notZeroRule = [
    (v) => v !== 0 || i18n.t("rules.not_zero")
];

export const greaterOrEqualsZeroRule = [
    (v) => v >= 0 || i18n.t("rules.greater_or_equals_zero")
];

export const numberRule = [
    (v) => !isNaN(+v) || i18n.t("rules.number_format")
];

export const oneHundredFieldLengthRule = [
    (v) => !v || v.length <= 100 || i18n.t("rules.one_hundred_length")
]