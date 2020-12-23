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

    private final BaseMapper<D> processFormMapper;
    private final ProcessFormItemMapper formItemMapper;

    public MybatisProcessFormRepository(BaseMapper<D> processFormMapper,
                                        ProcessFormItemMapper formItemMapper) {
        this.processFormMapper = processFormMapper;
        this.formItemMapper = formItemMapper;
    }

    @Override
    public F formOf(String instanceId) {
        F form = processFormOf(instanceId);
        form.setFormItem(processFormItemOf(instanceId));
        return form;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(F form) {
        saveProcessForm(form);
        saveBody(form);
    }

    /**
     * 将数据模型转换为领域对象
     *
     * @param processFormDo 数据模型
     * @return 领域对象
     */
    protected abstract F toProcessForm(D processFormDo);

    /**
     * 将领域模型转换为数据模型
     *
     * @param form 领域模型
     * @return 数据模型
     */
    protected abstract D toProcessFormDo(F form);

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
        formItemDo.setProcessInstanceId(form.getProcessInstanceId());
        return formItemDo;
    }

    private void saveProcessForm(F processForm) {
        D processFormDo = toProcessFormDo(processForm);

        BeanUtil.copyProperties(processForm, processFormDo);
        processFormDo.setUpdateTime(new Date());

        if (processFormDo.getId() == null) {
            processFormDo.setCreateTime(processFormDo.getUpdateTime());

            processFormMapper.insert(processFormDo);

            processForm.setId(processFormDo.getId());
            processForm.setCreateTime(processFormDo.getCreateTime());
        } else {
            processFormMapper.updateById(processFormDo);
        }

        processForm.setUpdateTime(processFormDo.getUpdateTime());
    }


    private ProcessFormItem processFormItemOf(String processInstanceId) {
        ProcessFormItemDo formItemDo = formItemMapper.selectOne(
                new LambdaQueryWrapper<ProcessFormItemDo>()
                        .eq(ProcessFormItemDo::getProcessInstanceId, processInstanceId)
        );

        if (formItemDo == null) {
            return null;
        }

        ProcessFormItem formItem = new ProcessFormItem();
        BeanUtil.copyProperties(formItemDo, formItem);
        return formItem;
    }

    private F processFormOf(String processInstanceId) {
        D processFormDo = processFormMapper.selectOne(
                new QueryWrapper<D>()
                        .eq("process_instance_id", processInstanceId)
        );

        F processForm = toProcessForm(processFormDo);
        BeanUtil.copyProperties(processFormDo, processForm);
        return processForm;
    }
}