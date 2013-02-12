

jQuery(function(){
        
        var j = jQuery;
        
        var addResultFrame = function() {
            var newId = 'rs' + new Date().getTime();
            var alert = '<div class="alert alert-info"><a class="close" data-dismiss="alert">×</a>';
            alert += '<iframe id="' + newId + '" width="100%" height="600px" frameBorder="0"> No Result </iframe>';
            alert += '</div>';
            j('#flash-holder').prepend(alert);
            j('#clear').removeClass('disabled');
            return newId;
        }
        
        j('#tplform').submit(function(){
           j('#tplform').attr('target', addResultFrame());
        });

        
        window.loadItem = function(name, readonly){
            j.get('/_ah/gaelyk-console/template/' + name, function(val){
                if(val.template){
                    aceeditor.getSession().setValue(val.script);
                    templateeditor.getSession().setValue(val.template);
                    doLoad(val, readonly);
                }
            });
        };
        
        j('#load').click(openBrowser('templates', 'Templates'));
        
        j('#save').click(function(){
            if(j(this).hasClass('disabled')){
                return;
            }
            j(this).addClass('disabled');
            var value = templateeditor.getSession().getValue();
            if(!value){
                showMessage('Saving failed!', 'error', 'Cannot save empty template!', true);
                return;
            }
            var name = j('#name').val();
            if(!name){
                showMessage('Saving failed!', 'error', 'You must specify the name of the template!', true);
                return;
            }
            j('#progress').show();
            var tags = j('#tags').val();
            var url = '/_ah/gaelyk-console/template/' + urlEncode(name) + '?foo=bar';
            if(tags){
                j.each(tags.replace(/\\s+/, '').split(','), function(i, val){
                    url += '&tags=' + urlEncode(val);
                });
            }
            j.post(url, 'template=' + urlEncode(value) + '&script=' + urlEncode(aceeditor.getSession().getValue() || ''), function(result){
                j('#progress').hide();
                if(result.ok){
                    showMessage('Template  "' + name + '" saved!', 'success', '', true);
                    window.location.hash = '#' + name
                    j('#save').addClass('disabled');
                    return;
                }
                if(result.exception){
                    showMessage('Saving failed!', 'error', result.exception, true);
                    return;
                }
                showMessage('Saving failed!', 'error', 'Unknow error. Please consult application logs.', true);
            });
        });

        
        j('#doDelete').click(function(){
            var name = j('#name').data('original-name');
            if(!name){
                showMessage('Deletion failed!', 'error', 'Cannot find the original name of the template to be deleted!', true);
                return;
            }
            j('#progress').show();
            j('#deleteModal').hide();
            j.ajax({
                type: 'DELETE',
                url: '/_ah/gaelyk-console/template/' + urlEncode(name),
                success: function(result){
                    j('#progress').hide();
                    if(result.deleted){
                        showMessage('Template "' + name + '" deleted!', 'success', '', true);
                        updateSaveButton(aceeditor);
                        j('#delete').addClass('disabled');
                        return;
                    }
                    if(result.exception){
                        showMessage('Deletion failed!', 'error', result.exception, true);
                        return;
                    }
                    showMessage('Deletion failed!', 'error', 'Unknow error. Please consult application logs.', true);
                }
            });
        });
    });