package org.team4u.workflow.infrastructure.persistence.form;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.transaction.annotation.Transactional;
import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.form.ProcessFormItem;
import org.team4u.workflow.domain.form.ProcessFormRepository;

import java.util.Date;

/**
 * 基于数据库的流程表单资源库
 *
 * @author jay.wu
 */
public abstract class MybatisProcessFormRepository<F extends ProcessForm, D extends ProcessFormDo>
        implements ProcessFormRepository<F> {

    private final ProcessFormItemMapper formItemMapper;

    public MybatisProcessFormRepository(ProcessFormItemMapper formItemMapper) {
        this.formItemMapper = formItemMapper;
    }

    @Override
    public F formOf(String formId) {
        F form = processFormOf(formId);
        form.setFormItem(processFormItemOf(formId));
        return form;
    }

    private ProcessFormItem processFormItemOf(String formId) {
        ProcessFormItemDo formItemDo = formItemMapper.selectOne(
                new LambdaQueryWrapper<ProcessFormItemDo>()
                        .eq(ProcessFormItemDo::getFormId, formId)
        );

        if (formItemDo == null) {
            return null;
        }

        ProcessFormItem formItem = new ProcessFormItem();
        BeanUtil.copyProperties(formItemDo, formItem);
        return formItem;
    }

    private F processFormOf(String formId) {
        D processFormDo = processFormMapper().selectOne(
                new QueryWrapper<D>()
                        .eq("form_id", formId)
        );

        F processForm = toProcessForm(processFormDo);
        BeanUtil.copyProperties(processFormDo, processForm);
        return processForm;
    }

    protected abstract F toProcessForm(D processFormDo);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(F form) {
        saveProcessForm(form);
        saveBody(form);
    }

    private void saveBody(F form) {
        if (form.getFormItem() == null) {
            return;
        }

        ProcessFormItemDo formItemDo = toProcessFormItemDo(form);
        formItemDo.setUpdateTime(new Date());

        if (formItemDo.getId() == null) {
            formItemDo.setCreateTime(formItemDo.getUpdateTime());

            formItemMapper.insert(formItemDo);

            form.getFormItem().setId(formItemDo.getId());
        } else {
            formItemMapper.updateById(formItemDo);
        }
    }

    private ProcessFormItemDo toProcessFormItemDo(F form) {
        ProcessFormItemDo formItemDo = new ProcessFormItemDo();
        BeanUtil.copyProperties(form.getFormItem(), formItemDo);
        formItemDo.setFormId(form.getFormId());
        return formItemDo;
    }

    private void saveProcessForm(F processForm) {
        D processFormDo = toProcessFormDo(processForm);

        BeanUtil.copyProperties(processForm, processFormDo);
        processFormDo.setUpdateTime(new Date());

        if (processFormDo.getId() == null) {
            processFormDo.setCreateTime(processFormDo.getUpdateTime());
            processFormMapper().insert(processFormDo);

            processForm.setId(processFormDo.getId());

            processForm.setCreateTime(processFormDo.getCreateTime());
        } else {
            processFormMapper().updateById(processFormDo);
        }

        processForm.setUpdateTime(processFormDo.getUpdateTime());
    }

    protected abstract BaseMapper<D> processFormMapper();

    protected abstract D toProcessFormDo(ProcessForm form);
}