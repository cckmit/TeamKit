package org.team4u.workflow.infrastructure.persistence.form;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.team4u.workflow.domain.form.FormIndex;
import org.team4u.workflow.domain.form.FormIndexRepository;

import java.util.Date;

/**
 * 基于数据库的表单索引资源库
 *
 * @author jay.wu
 */
public abstract class MybatisFormIndexRepository<F extends FormIndex, D extends FormIndexDo>
        implements FormIndexRepository<F> {

    private final BaseMapper<D> formIndexMapper;

    public MybatisFormIndexRepository(BaseMapper<D> formIndexMapper) {
        this.formIndexMapper = formIndexMapper;
    }

    @Override
    public F formOf(String instanceId) {
        return formIndexOf(instanceId);
    }

    @Override
    public void save(F formIndex) {
        D formIndexDo = toFormIndexDo(formIndex);

        BeanUtil.copyProperties(formIndex, formIndexDo);
        formIndexDo.setUpdateTime(new Date());

        if (formIndexDo.getId() == null) {
            formIndexDo.setCreateTime(formIndexDo.getUpdateTime());

            formIndexMapper.insert(formIndexDo);

            formIndex.setId(formIndexDo.getId());
            formIndex.setCreateTime(formIndexDo.getCreateTime());
        } else {
            formIndexMapper.updateById(formIndexDo);
        }

        formIndex.setUpdateTime(formIndexDo.getUpdateTime());
    }

    /**
     * 将数据模型转换为领域对象
     *
     * @param formIndexDo 数据模型
     * @return 领域对象
     */
    protected abstract F toFormIndex(D formIndexDo);

    /**
     * 将领域模型转换为数据模型
     *
     * @param formIndex 领域模型
     * @return 数据模型
     */
    protected abstract D toFormIndexDo(F formIndex);

    private F formIndexOf(String processInstanceId) {
        D formIndexDo = formIndexMapper.selectOne(
                new QueryWrapper<D>()
                        .eq("process_instance_id", processInstanceId)
        );

        F formIndex = toFormIndex(formIndexDo);
        BeanUtil.copyProperties(formIndexDo, formIndex);
        return formIndex;
    }
}