// from http://0061276.netsolhost.com/tony/javascript/urlEncode.js
function urlEncode(c) {
    return encodeURIComponent(c);
}

function showMessage(title, type, msg, noPre) {
    var j = jQuery;
    var alert = '<div class="alert alert-' + type + '"><a class="close" data-dismiss="alert">×</a>';
    alert += '<h4 class="alert-heading">' + title + '</h4>';
    if (msg) {
        if (!noPre) {
            alert += '<pre>'
        }
        alert += msg
        if (!noPre) {
            alert += '</pre>'
        }
    }
    alert += '</div>';
    j('#flash-holder').prepend(alert);
    j('#clear').removeClass('disabled');
}

jQuery(function(){
    
    var j = jQuery;
    
    window.doLoad = function(val, readonly){
        j('#myModal').hide();
        j('#name').val(val.name).data('original-name', val.name);
        window.location.hash = val.name; 
        if(val.tags){
            j('#tags').val(val.tags.join(', '));
        } else {
            j('#tags').val('');
        }
        j('#progress').hide();

        if(!readonly){
            j('#delete').removeClass('disabled');
            j('#save').addClass('disabled');


            j('#load').removeClass('disabled');
            j('#execute').removeClass('disabled');
        }
    }
    
    
    j('.closeLoad').click(function(){
        j('#myModal').hide();
        j('#load').removeClass('disabled');
        j('#execute').removeClass('disabled');
    });
    
    j('.closeDelete').click(function(){
        j('#deleteModal').hide(); 
    });
    
    
    j('#clear').click(function(){
        if(j(this).hasClass('disabled')){
            return;
        }
        j('#flash-holder').text('');
        j(this).addClass('disabled');
    });
    
    j('#delete').click(function(){
        if(j(this).hasClass('disabled')){
            return;
        }
        j('#deleteModal').show();
    });
    
    
    window.openBrowser = function(endpoint, headline, readonly) {
        return function(){
            if(j(this).hasClass('disabled')){
                return;
            }
            j('#myModal').show();
            j(this).addClass('disabled');
            j.get('/_ah/gaelyk-console/' + endpoint, function(scripts){
                j('.scriptBrowser').html('');
                j('.scriptBrowser').append('<li class="nav-header">'+ headline + '</li>');
                j.each(scripts, function(i, val){
                    var item = '<li><a class="script" data-script-name="' + val.name + '">';
                    item+= val.name;
                    if(val.tags){
                        j.each(val.tags, function(index, tag){
                            item+= '&nbsp;<span class="label pull-right">' + tag + '</span>';
                        });
                    }
                    item+= '</a>';
                    item+= '</li>';
                    j('.scriptBrowser').append(item);
                });
                j('a.script').click(function(event){
                    j('#progress').show();
                    var name = j(this).data('script-name');
                    window.loadItem(name, readonly);
                });
                j('#loading-scripts').hide();
            });
            
        }
    };
    
});