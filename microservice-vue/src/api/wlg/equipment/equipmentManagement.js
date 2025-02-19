import request from '@/utils/request'


export function findEquipmentManagementPage(conditions) {
    return request({
        url: '/wlg-equipment/equipmentManagement/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findEquipmentManagementById(equipmentManagementId) {
    return request({
        url: '/wlg-equipment/equipmentManagement/' + equipmentManagementId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteEquipmentManagement(deleteForm) {
    return request({
        url: '/wlg-equipment/equipmentManagement/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-equipment/equipmentManagement',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-equipment/equipmentManagement/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}


export function findMchNameList() {
    return request({
        url: '/wlg-equipment/equipmentManagement/findMchNameList',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
    })
}


export function findSpecListByMchName(conditions) {
    return request({
        url: '/wlg-equipment/equipmentManagement/findSpecListByMchName',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}


export function findTypeVersionListByMchNameAndSpec(conditions) {
    return request({
        url: '/wlg-equipment/equipmentManagement/findTypeVersionListByMchNameAndSpec',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findEquipmentByMchCode(params) {
    return request({
        url: '/wlg-equipment/equipmentManagement/findEquipmentByMchCode',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: params
    })
}

export function exportEquipmentExcel(conditions) {
    return request({
        url: '/wlg-equipment/equipmentManagement/exportEquipmentExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

export function convertUser(userObject, value) {
    let userInfo = [];
    userInfo.push(value);
    Object.keys(userObject).some((key) => {
        if (userObject[key].username.trim() === ('' + value)) {
            userInfo.push('（' + userObject[key].name + '）');
            return true;
        }
    })
    return userInfo.join('');
}

export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/wlg-equipment/equipmentManagement/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function downloadTemplate() {
    return request({
        url: '/wlg-equipment/equipmentManagement/downloadTemplate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
    })
}