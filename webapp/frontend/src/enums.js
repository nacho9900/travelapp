import i18n from './i18n/i18n.js';

export const memberRoles = [
    { value: "OWNER", text: i18n.t("enums.member_roles.owner"), color: "yellow lighten-3" },
    { value: "ADMIN", text: i18n.t("enums.member_roles.admin"), color: "orange lighten-3" },
    { value: "MEMBER", text: i18n.t("enums.member_roles.member"), color: "blue lighten-3" },
];