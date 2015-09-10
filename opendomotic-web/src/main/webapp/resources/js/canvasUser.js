window.onload = initUser;

function initUser() {
    connect();
}

function drawCanvas(idEnvironment) {
    initCanvas(false, mouseUpDevice, idEnvironment);
}

//------------------------------------------------------------------------------

function mouseUpDevice() {
    switch (devicePressed.type) {            
        case DeviceType.VALUE:
            newValue = prompt(devicePressed.name, devicePressed.value);
            if (newValue !== null) {
                $.getJSON(getUrl('rest/device/set?name=' + devicePressed.name + '&value='+newValue), null, null);
                appendCustomScript();
            }            
            break;
        
        case DeviceType.SWITCH_CONFIRM:
            switched = devicePressed.value === '1' || devicePressed.value === 1;
            if (!confirm((switched ? 'Desligar ' : 'Ligar ') + devicePressed.name + '?'))
                break;
        
        case DeviceType.SWITCH:
            $.getJSON(getUrl('rest/device/switch?name=' + devicePressed.name), null, null);
            appendCustomScript();
            break;
        
        case DeviceType.CUSTOM:
            appendCustomScript();
            break;
        
        default: //DeviceType.SENSOR:
            alert(devicePressed.name + ' = ' + devicePressed.value);
            appendCustomScript();
            break;
    }
    
}

function appendCustomScript() {
    if (devicePressed.customScript !== null && devicePressed.customScript !== '') {
        $('head').append($('<script>'+devicePressed.customScript+'</script>'));
    }
}