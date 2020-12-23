package org.team4u.workflow.infrastructure.persistence.form;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.transaction.annotation.Transactional;
import org.team4u.workflow.domain.form.ProcessForm;
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

    public MybatisProcessFormRepository(BaseMapper<D> processFormMapper) {
        this.processFormMapper = processFormMapper;
    }

    @Override
    public F formOf(String instanceId) {
        return processFormOf(instanceId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(F form) {
        saveProcessForm(form);
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