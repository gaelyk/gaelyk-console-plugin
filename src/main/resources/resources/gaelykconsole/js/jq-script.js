

    jQuery(function(){      
        var j = jQuery;
        
        window.loadItem = function(name, readonly){
            j.get('/_ah/gaelyk-console/script/' + name, function(val){
                if(val && val.text){
                    aceeditor.getSession().setValue(val.text);
                    doLoad(val, readonly);
                }
            });
        };
        
        j('#execute').click(function(){
            if(j(this).hasClass('disabled')){
                return;
            }
            j('#progress').show();
            j(this).addClass('disabled');
            var script = aceeditor.getSession().getValue();
            script = urlEncode(script);
            j.post('/_ah/gaelyk-console/script/execute?channel=' + window.chid, 'text=' + script, function(result){
                j('#progress').hide();
                j('#execute').removeClass('disabled');
                if(result.output){
                    showMessage('Script Output', 'info', result.output);
                } 
                if(result.result){
                    showMessage('Script Result', 'success', result.result);
                }
                if(result.exception){
                    showMessage('Script Failed', 'error', result.exception);
                }
                if(!result.result && !result.output && !result.exception){
                    showMessage('Script Result', 'success', 'Execution finished without any result', true);
                }
            }).error(function(){
                showMessage('Script Failed', 'error', 'Unknown Exception thrown. See Application log for details.');
            });
        });
        
        j('#load').click(openBrowser('scripts', 'Scripts'));
        
        j('#save').click(function(){
            if(j(this).hasClass('disabled')){
                return;
            }
            j(this).addClass('disabled');
            var name = j('#name').val();
            if(!name){
                showMessage('Saving failed!', 'error', 'You must specify the name of the script!', true);
                return;
            }
            var value = aceeditor.getSession().getValue();
            if(!value){
                showMessage('Saving failed!', 'error', 'Cannot save empty script!', true);
                return;
            }
            j('#progress').show();
            var tags = j('#tags').val();
            var url = '/_ah/gaelyk-console/script/' + urlEncode(name) + '?foo=bar';
            if(tags){
                j.each(tags.replace(/\\s+/, '').split(','), function(i, val){
                    url += '&tags=' + urlEncode(val);
                });
            }
            j.post(url, 'text=' + urlEncode(value), function(result){
                j('#progress').hide();
                if(result.ok){
                    showMessage('Script "' + name + '" saved!', 'success', '', true);
                    j('#save').addClass('disabled');
                    window.location.hash = '#' + name
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
                showMessage('Deletion failed!', 'error', 'Cannot find the original name of the script to be deleted!', true);
                return;
            }
            j('#progress').show();
            j('#deleteModal').hide();
            j.ajax({
                type: 'DELETE',
                url: '/_ah/gaelyk-console/script/' + urlEncode(name),
                success: function(result){
                    j('#progress').hide();
                    if(result.deleted){
                        showMessage('Script "' + name + '" deleted!', 'success', '', true);
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