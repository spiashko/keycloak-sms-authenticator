<#import "sms-template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "title">
        ${msg("loginTitle",realm.name)}
    <#elseif section = "header">
        ${msg("mobile_number.header",realm.name)}
    <#elseif section = "form">
        <form id="kc-resend-sms-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcInputWrapperClass!}">
                    <input id="mobileNumber" type="tel" class="${properties.kcInputClass!}"/>
                </div>
            </div>
            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}">
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <div class="${properties.kcFormButtonsWrapperClass!}">
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                               name="resend_sms_submit" id="kc-login" type="submit"
                               value="${msg("resend_sms.submit")}"/>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                               name="resend_sms_cancel" id="kc-cancel" type="submit"
                               value="${msg("resend_sms.cancel")}"/>
                    </div>
                </div>
            </div>
        </form>

        <style type="text/css">
            .iti {
                width: 100%;
            }

            .iti__flag {
                background-image: url("${url.resourcesPath}/flags.png");
            }

            @media (-webkit-min-device-pixel-ratio: 2), (min-resolution: 192dpi) {
                .iti__flag {
                    background-image: url("${url.resourcesPath}/flags@2x.png");
                }
            }
        </style>

        <script src="${url.resourcesPath}/intlTelInput.min.js"></script>
        <script>
            var iti = document.querySelector("#mobileNumber");
            window.intlTelInput(iti, {
                hiddenInput: "mobile_number",
                placeholderNumberType: "MOBILE",
                preferredCountries: ['us'],
                separateDialCode: true,
                utilsScript: "${url.resourcesPath}/utils.js",
            });
        </script>
    </#if>
</@layout.registrationLayout>