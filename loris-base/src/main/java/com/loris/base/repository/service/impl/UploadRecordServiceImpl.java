package com.loris.base.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.base.bean.UploadRecord;
import com.loris.base.repository.mapper.UploadRecordMapper;
import com.loris.base.repository.service.UploadRecordService;

@Service("uploadRecordService")
public class UploadRecordServiceImpl extends ServiceImpl<UploadRecordMapper, UploadRecord>
	implements UploadRecordService
{

}
