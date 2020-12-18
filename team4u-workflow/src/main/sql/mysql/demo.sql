# 待审批列表
select pi.current_node_id, pi.process_definition_id, f.*
from process_instance pi,
     process_assignee pa,
     test_form f
where pi.process_instance_id = f.process_instance_id
  and pi.process_instance_id = pa.process_instance_id
  and pi.current_node_id = pa.process_node_id
  and pa.process_action != ''
  and pa.assignee = 'jay';

# 历史审批列表
select pi.current_node_id, pi.process_definition_id, f.*
from process_instance pi,
     process_assignee pa,
     test_form f
where pi.process_instance_id = f.process_instance_id
  and pi.process_instance_id = pa.process_instance_id
  and pa.process_action != ''
  and pa.assignee = 'jay';

# 申请列表
select pi.current_node_id, pi.process_definition_id, f.*
from process_instance pi,
     test_form f
where pi.process_instance_id = f.process_instance_id
  and pi.created_by = 'jay'