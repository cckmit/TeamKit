package org.team4u.workflow.infrastructure.persistence.form;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.transaction.annotation.Transactional;
import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.form.ProcessFormHeader;
import org.team4u.workflow.domain.form.ProcessFormItem;
import org.team4u.workflow.domain.form.ProcessFormRepository;

import java.util.Date;

/**
 * 基于数据库的流程表单资源库
 *
 * @author jay.wu
 */
public class MybatisProcessFormRepository<H extends ProcessFormHeader> implements ProcessFormRepository {

    private final BaseMapper<H> formHeaderMapper;
    private final ProcessFormItemMapper formItemMapper;

    public MybatisProcessFormRepository(BaseMapper<H> formHeaderMapper,
                                        ProcessFormItemMapper formItemMapper) {
        this.formHeaderMapper = formHeaderMapper;
        this.formItemMapper = formItemMapper;
    }

    @Override
    public ProcessForm formOf(String formId) {
        ProcessForm form = new ProcessForm();
        form.setFormHeader(formHeaderOf(formId));
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

    private ProcessFormHeader formHeaderOf(String formId) {
        return formHeaderMapper.selectOne(
                new QueryWrapper<H>().eq("form_id", formId)
        );
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(ProcessForm form) {
        saveHeader((H) form.getFormHeader());
        saveBody(form.getFormItem());
    }

    private void saveBody(ProcessFormItem formItem) {
        if (formItem == null) {
            return;
        }

        ProcessFormItemDo formItemDo = toProcessFormItemDo(formItem);
        if (formItemDo.getId() == null) {
            formItemDo.setCreateTime(new Date());
            formItemDo.setUpdateTime(new Date());
            formItemMapper.insert(formItemDo);
            formItem.setId(formItemDo.getId());
        } else {
            formItemDo.setUpdateTime(new Date());
            formItemMapper.updateById(formItemDo);
        }
    }

    private ProcessFormItemDo toProcessFormItemDo(ProcessFormItem formItem) {
        ProcessFormItemDo formItemDo = new ProcessFormItemDo();
        BeanUtil.copyProperties(formItem, formItemDo);
        return formItemDo;
    }

    private void saveHeader(H formHeader) {
        if (formHeader.getId() == null) {
            ReflectUtil.setFieldValue(formHeader, "createTime", new Date());
            ReflectUtil.setFieldValue(formHeader, "updateTime", new Date());
            formHeaderMapper.insert(formHeader);
        } else {
            ReflectUtil.setFieldValue(formHeader, "updateTime", new Date());
            formHeaderMapper.updateById(formHeader);
        }
    }
}