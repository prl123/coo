<div class="page">
    <div class="pageContent">
        <@s.form action="/system/role-update" cssClass="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone)">
         <@s.hidden path="role.id" />
        <div class="pageFormContent" layoutH="60">
			<dl>
				<dt>名称：</dt>
				<dd><@s.input path="role.name" size="30" maxlength="60" class="required" /></dd>
			</dl>
			<div class="divider" />
            <@system.permissions permissionGroups />
        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">保存</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
        </@s.form>
    </div>
</div>